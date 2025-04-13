package ru.alex0d.investapp.domain.models

import ru.alex0d.investapp.utils.AuroraExport

@AuroraExport
data class Candle(
    val timestamp: Long,
    val open: Double,
    val close: Double,
    val high: Double,
    val low: Double,
    val volume: Long
)