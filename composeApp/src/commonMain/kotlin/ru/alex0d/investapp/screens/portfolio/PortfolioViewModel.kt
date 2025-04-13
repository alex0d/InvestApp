package ru.alex0d.investapp.screens.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.alex0d.investapp.data.repositories.PortfolioRepository
import ru.alex0d.investapp.domain.models.PortfolioInfo

class PortfolioViewModel(
    private val portfolioRepository: PortfolioRepository
) : ViewModel() {

    private val _state = MutableStateFlow<PortfolioState>(PortfolioState.Loading)
    val state: StateFlow<PortfolioState> = _state.asStateFlow()

    private var updateJob: Job? = null

    fun startUpdating() {
        updateJob = viewModelScope.launch {
            while (isActive) {
                updatePortfolioState()
                delay(30000)
            }
        }
    }

    fun stopUpdating() {
        updateJob?.cancel()
        updateJob = null
    }

    private suspend fun updatePortfolioState() {
        val portfolioInfo = portfolioRepository.getPortfolio()
        portfolioInfo?.let {
            _state.value = PortfolioState.PortfolioInfoFetched(it)
        } ?: run {
            _state.value = PortfolioState.Error
        }
    }
}

sealed class PortfolioState {
    object Loading : PortfolioState()
    data class PortfolioInfoFetched(val portfolioInfo: PortfolioInfo) : PortfolioState()
    object Error : PortfolioState()
}