package ru.alex0d.investapp.screens.stock.chart

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class ChartAdapterFactory {
    actual fun <T : ChartModel> createAdapter(): ChartAdapter<T> {
        throw NotImplementedError("Will never be implemented in JS target")
    }
}