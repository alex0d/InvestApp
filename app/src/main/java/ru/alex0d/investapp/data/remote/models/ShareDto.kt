package ru.alex0d.investapp.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ShareDto(
    val uid: String,
    val figi: String,
    val ticker: String,
    val classCode: String,
    val isin: String,
    val currency: String,
    val name: String,
    val countryOfRisk: String,
    val countryOfRiskName: String,
    val sector: String,
    val lastPrice: Double,
    val url: String,
    val textColor: String,
    val backgroundColor: String,
)