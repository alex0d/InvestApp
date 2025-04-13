package ru.alex0d.investapp.screens.stock.chart

import ru.alex0d.investapp.screens.stock.chart.linear.LinearChartAdapter

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "UNCHECKED_CAST")
actual class ChartAdapterFactory {
    actual fun <T: ChartModel> createAdapter(): ChartAdapter<T> {
        return LinearChartAdapter() as ChartAdapter<T>
    }
}