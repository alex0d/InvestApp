package ru.alex0d.investapp.domain.models

sealed class ChartData {
    abstract val timestamps: List<Long>
    abstract val close: List<Double>

    data class Line(
        override val timestamps: List<Long>,
        override val close: List<Double>
    ) : ChartData() {
        companion object
    }

    data class Candlestick(
        override val timestamps: List<Long>,
        override val close: List<Double>,
        val open: List<Double>,
        val high: List<Double>,
        val low: List<Double>
    ) : ChartData() {
        companion object
    }
}

fun ChartData.Line.Companion.fromCandles(candles: List<Candle>): ChartData.Line {
    return ChartData.Line(
        timestamps = candles.map { it.timestamp },
        close = candles.map { it.close }
    )
}

fun ChartData.Candlestick.Companion.fromCandles(candles: List<Candle>): ChartData.Candlestick {
    return ChartData.Candlestick(
        timestamps = candles.map { it.timestamp },
        open = candles.map { it.open },
        close = candles.map { it.close },
        high = candles.map { it.high },
        low = candles.map { it.low }
    )
}