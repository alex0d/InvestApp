package ru.alex0d.investapp.screens.tarot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.alex0d.investapp.data.repositories.TarotRepository
import ru.alex0d.investapp.domain.models.TarotPrediction

class TarotViewModel(
    private val tarotRepository: TarotRepository,
    private val stockName: String = ""
) : ViewModel() {
    private val _state = MutableStateFlow<TarotPredictionState>(TarotPredictionState.Loading)
    val state: StateFlow<TarotPredictionState> = _state.asStateFlow()

    init {
        fetchTarotPrediction()
    }

    private fun fetchTarotPrediction() {
        viewModelScope.launch {
            val prediction = tarotRepository.getPrediction(stockName)

            if (prediction != null) {
                _state.value = TarotPredictionState.Success(prediction)
            } else {
                _state.value = TarotPredictionState.Error("An error occurred")
            }
        }
    }
}

sealed class TarotPredictionState {
    object Loading : TarotPredictionState()
    data class Success(val prediction: TarotPrediction) : TarotPredictionState()
    data class Error(val message: String) : TarotPredictionState()
}