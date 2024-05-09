package ru.alex0d.investapp.domain.models

data class Share(
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
