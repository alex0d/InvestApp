package ru.alex0d.investapp.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.alex0d.investapp.data.repositories.StockRepository
import ru.alex0d.investapp.domain.models.Share
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
class SearchViewModel(
    private val stockRepository: StockRepository
) : ViewModel() {

    private val _searchFieldState = MutableStateFlow<SearchFieldState>(SearchFieldState.Idle)
    val searchFieldState: StateFlow<SearchFieldState> = _searchFieldState

    private val _viewState = MutableStateFlow<ViewState>(ViewState.IdleScreen)
    val viewState: StateFlow<ViewState> = _viewState

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    private val isInitialized = AtomicBoolean(false)

    @OptIn(FlowPreview::class)
    fun initialize() {
        if (isInitialized.compareAndSet(false, true)) {
            viewModelScope.launch {
                inputText.debounce(500).collectLatest { input ->
                    if (input.blankOrEmpty()) {
                        _viewState.update { ViewState.IdleScreen }
                        return@collectLatest
                    }

                    stockRepository.getSharesByTicker(input)?.let { shares ->
                        if (shares.isEmpty()) {
                            _viewState.update { ViewState.NoResults }
                        } else {
                            _viewState.update { ViewState.SearchResultsFetched(shares) }
                        }
                    } ?: run {
                        _viewState.update { ViewState.Error }
                    }
                }
            }
        }
    }

    fun updateInput(inputText: String) {
        _inputText.update { inputText }
        activateSearchField()

        if (inputText.blankOrEmpty().not()) {
            _viewState.update { ViewState.Loading }
        }
    }

    fun searchFieldActivated() {
        activateSearchField()
    }

    fun clearInput() {
        _viewState.update { ViewState.Loading }
        _inputText.update { "" }
        _searchFieldState.update { SearchFieldState.EmptyActive }
    }

    fun revertToInitialState() {
        _viewState.update { ViewState.IdleScreen }
        _inputText.update { "" }
        _searchFieldState.update { SearchFieldState.Idle }
    }

    fun keyboardHidden() {
        if (inputText.value.blankOrEmpty()) {
            _searchFieldState.update { SearchFieldState.EmptyActive }
        } else {
            _searchFieldState.update { SearchFieldState.WithInputActive }
        }
    }

    private fun activateSearchField() {
        if (inputText.value.blankOrEmpty().not()) {
            _searchFieldState.update { SearchFieldState.WithInputActive }
        } else {
            _searchFieldState.update { SearchFieldState.EmptyActive }
        }
    }

    private fun String.blankOrEmpty() = this.isBlank() || this.isEmpty()

    sealed interface ViewState {
        object IdleScreen : ViewState
        object Loading : ViewState
        object Error : ViewState
        object NoResults : ViewState
        data class SearchResultsFetched(val results: List<Share>) : ViewState
    }

    sealed interface SearchFieldState {
        object Idle : SearchFieldState
        object EmptyActive : SearchFieldState
        object WithInputActive : SearchFieldState
    }
}
