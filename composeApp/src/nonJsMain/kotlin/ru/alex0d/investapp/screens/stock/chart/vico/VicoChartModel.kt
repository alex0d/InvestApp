package ru.alex0d.investapp.screens.stock.chart.vico

import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import ru.alex0d.investapp.screens.stock.chart.ChartModel

class VicoChartModel(
    val modelProducer: CartesianChartModelProducer,
) : ChartModel