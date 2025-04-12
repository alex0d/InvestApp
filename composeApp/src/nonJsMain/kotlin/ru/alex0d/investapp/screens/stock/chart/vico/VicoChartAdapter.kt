package ru.alex0d.investapp.screens.stock.chart.vico

import com.patrykandpatrick.vico.multiplatform.cartesian.data.CandlestickCartesianLayerModel
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.LineCartesianLayerModel
import ru.alex0d.investapp.domain.models.ChartData
import ru.alex0d.investapp.screens.stock.chart.ChartAdapter

class VicoChartAdapter : ChartAdapter<VicoChartModel> {
    private val modelProducer = CartesianChartModelProducer()

    override suspend fun processChartData(chartData: ChartData) {
        val layerModel = when (chartData) {
            is ChartData.Line -> {
                LineCartesianLayerModel.partial {
                    series(x = chartData.timestamps, y = chartData.close)
                }
            }

            is ChartData.Candlestick -> {
                CandlestickCartesianLayerModel.partial(
                    x = chartData.timestamps,
                    opening = chartData.open,
                    closing = chartData.close,
                    high = chartData.high,
                    low = chartData.low
                )
            }
        }

        modelProducer.runTransaction {
            add(layerModel)
        }
    }

    override fun getChartModel() = VicoChartModel(modelProducer)

}