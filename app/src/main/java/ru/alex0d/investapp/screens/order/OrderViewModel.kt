package ru.alex0d.investapp.screens.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.alex0d.investapp.data.repositories.PortfolioRepository
import ru.alex0d.investapp.data.repositories.StockRepository
import ru.alex0d.investapp.domain.models.Share
import ru.alex0d.investapp.utils.toCurrencyFormat
import ru.alex0d.investapp.utils.toIntOrZero

class OrderViewModel(
    private val stockRepository: StockRepository,
    private val portfolioRepository: PortfolioRepository,
    private val orderAction: OrderAction,
    private val stockUid: String = ""
): ViewModel() {

    private val _state = MutableStateFlow<OrderDetailsState>(OrderDetailsState.Loading)
    val state: StateFlow<OrderDetailsState> = _state

    private val _availableLots = MutableStateFlow(0)
    val availableLots: StateFlow<Int> = _availableLots

    private val _lotsInput = MutableStateFlow("0")
    val lotsInput: StateFlow<String> = _lotsInput

    private val _totalValue = MutableStateFlow("")
    val totalValue: StateFlow<String> = _totalValue

    init {
        fetchOrderDetails()
    }

    private fun fetchOrderDetails() {
        _state.value = OrderDetailsState.Loading
        viewModelScope.launch {
            val share = stockRepository.getShareByUid(stockUid)
            share?.let {
                _state.value = OrderDetailsState.OrderDetailsFetched(share)

                if (orderAction == OrderAction.SELL) {
                    val portfolioStockInfo = portfolioRepository.getPortfolio()?.stocks?.find { it.uid == stockUid }
                    portfolioStockInfo?.let {
                        _availableLots.value = it.amount / share.lot
                    } ?: run {
                        _state.value = OrderDetailsState.Error
                    }
                }
            } ?: run {
                _state.value = OrderDetailsState.Error
            }
        }
    }

    fun increaseLots() {
        val lotsInt = lotsInput.value.toIntOrZero()
        updateLotsInput((lotsInt + 1).toString())
    }

    fun decreaseLots() {
        val lotsInt = lotsInput.value.toIntOrZero()
        updateLotsInput((lotsInt - 1).toString())
    }

    fun updateLotsInput(lots: String) {
        var newLots = lots.toIntOrZero()
        newLots = maxOf(0, newLots)

        if (orderAction == OrderAction.SELL && newLots > availableLots.value) {
            newLots = availableLots.value
        }

        _lotsInput.value = if(lots.isNotEmpty()) newLots.toString() else ""

        val share = (state.value as? OrderDetailsState.OrderDetailsFetched)?.share
        share?.let {
            _totalValue.value = (newLots * share.lot * share.lastPrice).toCurrencyFormat("RUB")
        }
    }

    suspend fun confirmOrder(): Boolean {
        val lotsInt = lotsInput.value.toIntOrZero()
        if (lotsInt == 0) return false

        val share = (state.value as? OrderDetailsState.OrderDetailsFetched)?.share
        share?.let {
            val amount = lotsInt * share.lot
            return if (orderAction == OrderAction.BUY) {
                portfolioRepository.buyStock(uid = share.uid, amount = amount)
            } else {
                portfolioRepository.sellStock(uid = share.uid, amount = amount)
            }
        }
        return false
    }

}

enum class OrderAction {
    BUY,
    SELL
}

sealed class OrderDetailsState {
    object Loading : OrderDetailsState()
    data class OrderDetailsFetched(val share: Share) : OrderDetailsState()
    object Error : OrderDetailsState()
}