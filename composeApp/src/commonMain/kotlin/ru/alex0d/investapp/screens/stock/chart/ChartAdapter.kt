package ru.alex0d.investapp.screens.stock.chart

import ru.alex0d.investapp.domain.models.ChartData

interface ChartModel

object EmptyChartModel : ChartModel

interface ChartAdapter<T: ChartModel> {
    suspend fun processChartData(chartData: ChartData)
    fun getChartModel(): T
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class ChartAdapterFactory {
    fun <T: ChartModel> createAdapter(): ChartAdapter<T>
}