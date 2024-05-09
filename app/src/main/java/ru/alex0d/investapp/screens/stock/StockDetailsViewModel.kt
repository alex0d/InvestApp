package ru.alex0d.investapp.screens.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.alex0d.investapp.data.repositories.PortfolioRepository
import ru.alex0d.investapp.data.repositories.StockRepository
import ru.alex0d.investapp.domain.models.PortfolioStockInfo
import ru.alex0d.investapp.domain.models.Share

class StockDetailsViewModel(
    private val stockRepository: StockRepository,
    private val portfolioRepository: PortfolioRepository,
    private val stockUid: String = ""
) : ViewModel() {

    private val _state = MutableStateFlow<StockDetailsState>(StockDetailsState.Loading)
    val state: StateFlow<StockDetailsState> = _state.asStateFlow()

    init {
        fetchStockDetails()
    }

    private fun fetchStockDetails() {
        viewModelScope.launch {
            try {
                val share = stockRepository.getShareByUid(stockUid)
                val portfolio = portfolioRepository.getPortfolio()
                val stockInPortfolio = portfolio.stocks.firstOrNull { it.uid == stockUid }
                _state.value = StockDetailsState.Success(share!!, stockInPortfolio)
            } catch (e: Exception) {
                _state.value = StockDetailsState.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }
}

sealed class StockDetailsState {
    object Loading : StockDetailsState()
    data class Success(val share: Share, val stockInfo: PortfolioStockInfo?) : StockDetailsState()
    data class Error(val message: String) : StockDetailsState()
}