package ru.alex0d.investapp.domain.models

import ru.alex0d.investapp.utils.AuroraExport

@AuroraExport
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
    val lot: Int,
    val url: String,
    val textColor: String,
    val backgroundColor: String,
)
