package ru.alex0d.investapp.screens.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CandlestickCartesianLayerModel
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.LineCartesianLayerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import ru.alex0d.investapp.data.repositories.MarketRepository
import ru.alex0d.investapp.data.repositories.PortfolioRepository
import ru.alex0d.investapp.data.repositories.StockRepository
import ru.alex0d.investapp.domain.models.Candle
import ru.alex0d.investapp.domain.models.CandleInterval
import ru.alex0d.investapp.domain.models.PortfolioStockInfo
import ru.alex0d.investapp.domain.models.Share

class StockDetailsViewModel(
    private val stockRepository: StockRepository,
    private val marketRepository: MarketRepository,
    private val portfolioRepository: PortfolioRepository,
    private val stockUid: String = ""
) : ViewModel() {
    private val clock = Clock.System

    private val _state = MutableStateFlow<StockDetailsState>(StockDetailsState.Loading)
    val state: StateFlow<StockDetailsState> = _state.asStateFlow()

    var chartType = ChartType.LINE
        set(value) {
            field = value
            updateChartModel(candles)
        }

    val modelProducer = CartesianChartModelProducer()
    private val candles = mutableListOf<Candle>()

    init {
        fetchStockDetails()
        fetchCandles(CandleInterval.INTERVAL_DAY)
    }

    fun fetchStockDetails() {
        viewModelScope.launch {
            try {
                val share = stockRepository.getShareByUid(stockUid)
                val portfolio = portfolioRepository.getPortfolio()
                val stockInPortfolio = portfolio?.stocks?.firstOrNull { it.uid == stockUid }
                _state.value = StockDetailsState.Success(share!!, stockInPortfolio)
            } catch (e: Exception) {
                _state.value = StockDetailsState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun fetchCandles(interval: CandleInterval) {
        viewModelScope.launch {
            val newCandles = marketRepository.getCandles(
                uid = stockUid,
                from = getFirstTimestamp(interval),
                to = clock.now().epochSeconds,
                interval = interval
            )
            newCandles?.let {
                candles.clear()
                candles.addAll(it)

                updateChartModel(it)
            }
        }
    }

    private fun updateChartModel(candles: List<Candle>) {
        val layerModel = if (chartType == ChartType.LINE) {
            LineCartesianLayerModel.partial {
                series(
                    x = candles.map { it.timestamp },
                    y = candles.map { it.close }
                )
            }
        } else {
            CandlestickCartesianLayerModel.partial(
                x = candles.map { it.timestamp },
                opening = candles.map { it.open },
                closing = candles.map { it.close },
                high = candles.map { it.high },
                low = candles.map { it.low }
            )
        }
        viewModelScope.launch(Dispatchers.Default) {
            modelProducer.runTransaction {
                add(layerModel)
            }
        }
    }

    private fun getFirstTimestamp(interval: CandleInterval): Long {
        val now = clock.now()
        val timeZone = TimeZone.currentSystemDefault()

        val instant = when {
            interval.ordinal <= 5 -> {  // 1min - 15min
                now.minus(1, DateTimeUnit.DAY, timeZone)
            }
            interval.ordinal == 6 -> { // 30min
                now.minus(2, DateTimeUnit.DAY, timeZone)
            }
            interval.ordinal == 7 -> { // hour
                now.minus(7, DateTimeUnit.DAY, timeZone)  // week
            }
            interval.ordinal <= 9 -> { // 2hour - 4hour
                now.minus(1, DateTimeUnit.MONTH, timeZone)
            }
            interval.ordinal == 10 -> { // day
                now.minus(1, DateTimeUnit.YEAR, timeZone)
            }
            else -> {
                now.minus(2, DateTimeUnit.YEAR, timeZone)
            }
        }

        return instant.epochSeconds
    }

}

sealed class StockDetailsState {
    object Loading : StockDetailsState()
    data class Success(val share: Share, val stockInfo: PortfolioStockInfo?) : StockDetailsState()
    data class Error(val message: String) : StockDetailsState()
}

enum class ChartType {
    CANDLES,
    LINE
}