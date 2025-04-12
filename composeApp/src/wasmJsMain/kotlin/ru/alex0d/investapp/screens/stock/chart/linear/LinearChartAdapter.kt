package ru.alex0d.investapp.screens.stock.chart.linear

import ru.alex0d.investapp.domain.models.ChartData
import ru.alex0d.investapp.screens.stock.chart.ChartAdapter

class LinearChartAdapter : ChartAdapter<LinearChartModel> {
    private val chartModel = LinearChartModel()

    override suspend fun processChartData(chartData: ChartData) {
        chartModel.timestamps.clear()
        chartModel.timestamps.addAll(chartData.timestamps)

        chartModel.values.clear()
        chartModel.values.addAll(chartData.close)
    }

    override fun getChartModel(): LinearChartModel = chartModel
}