package ru.alex0d.investapp.screens.portfolio

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.alex0d.investapp.data.PortfolioRepository
import ru.alex0d.investapp.data.remote.models.PortfolioInfoDto

class PortfolioViewModel(
    private val portfolioRepository: PortfolioRepository
) : ViewModel() {
    private val _portfolioState = MutableStateFlow<PortfolioInfoDto>(PortfolioInfoDto(0.0, 0.0, 0.0, emptyList()))
    val portfolioState: StateFlow<PortfolioInfoDto> = _portfolioState.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                Log.d("PortfolioViewModel", "Refreshing portfolio")
                _portfolioState.value = portfolioRepository.getPortfolio()
                delay(5000)
            }
        }
    }
}