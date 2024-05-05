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
import ru.alex0d.investapp.data.PortfolioRepository
import ru.alex0d.investapp.domain.models.PortfolioInfo

class PortfolioViewModel(
    private val portfolioRepository: PortfolioRepository
) : ViewModel() {
    private val _portfolioState =
        MutableStateFlow<PortfolioInfo>(PortfolioInfo(0.0, 0.0, 0.0, emptyList()))
    val portfolioState: StateFlow<PortfolioInfo> = _portfolioState.asStateFlow()

    private var updateJob: Job? = null

    fun startUpdating() {
        updateJob = viewModelScope.launch {
            while (isActive) {
                updatePortfolioState()
                delay(50000)
            }
        }
    }

    fun stopUpdating() {
        updateJob?.cancel()
        updateJob = null
    }

    private suspend fun updatePortfolioState() {
        val portfolioInfo = portfolioRepository.getPortfolio()
        _portfolioState.value = portfolioInfo
    }
}