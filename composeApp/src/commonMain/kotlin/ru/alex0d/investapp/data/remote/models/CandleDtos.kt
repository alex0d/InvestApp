package ru.alex0d.investapp.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class CandleRequest(
    val uid: String,
    val from: Long,
    val to: Long,
    val interval: String
)

@Serializable
data class CandleDto(
    val timestamp: Long,
    val open: Double,
    val close: Double,
    val high: Double,
    val low: Double,
    val volume: Long
)