package ru.alex0d.investapp.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class TarotPredictionDto(
    val cardName: String,
    val prediction: String
)