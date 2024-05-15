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

class OrderViewModel(
    private val stockRepository: StockRepository,
    private val portfolioRepository: PortfolioRepository,
    private val orderAction: OrderAction,
    private val stockUid: String = ""
): ViewModel() {

    private val _state = MutableStateFlow<OrderDetailsState>(OrderDetailsState.Loading)
    val state: StateFlow<OrderDetailsState> = _state

    private val _availableLots = MutableStateFlow<Int>(0)
    val availableLots: StateFlow<Int> = _availableLots

    private val _inputLots = MutableStateFlow<Int>(0)
    val inputLots: StateFlow<Int> = _inputLots

    private val _totalValue = MutableStateFlow<String>("")
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
        updateInputLots((inputLots.value + 1).toString())
    }

    fun decreaseLots() {
        updateInputLots((inputLots.value - 1).toString())
    }

    fun updateInputLots(lots: String) {
        var newLots = lots.toIntOrNull() ?: 0
        newLots = maxOf(0, newLots)

        _inputLots.value = if (orderAction == OrderAction.SELL && newLots > availableLots.value) {
            availableLots.value
        } else {
            newLots
        }

        val share = (state.value as? OrderDetailsState.OrderDetailsFetched)?.share
        share?.let {
            _totalValue.value = (inputLots.value * share.lot * share.lastPrice).toCurrencyFormat("RUB")
        }
    }

    suspend fun confirmOrder(): Boolean {
        val share = (state.value as? OrderDetailsState.OrderDetailsFetched)?.share
        share?.let {
            val amount = inputLots.value * share.lot
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