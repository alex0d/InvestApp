package ru.alex0d.investapp.screens.stock.chart.linear

import androidx.compose.runtime.mutableStateListOf
import ru.alex0d.investapp.screens.stock.chart.ChartModel

class LinearChartModel : ChartModel {
    val timestamps = mutableStateListOf<Long>()
    val values = mutableStateListOf<Double>()
}