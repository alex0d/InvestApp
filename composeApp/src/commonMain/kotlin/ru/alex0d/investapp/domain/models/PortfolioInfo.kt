package ru.alex0d.investapp.domain.models

import ru.alex0d.investapp.utils.AuroraExport

@AuroraExport
data class PortfolioInfo(
    val totalValue: Double,
    val totalProfit: Double,
    val totalProfitPercent: Double,
    val stocks: List<PortfolioStockInfo>
)

@AuroraExport
data class PortfolioStockInfo(
    val uid: String,
    val ticker: String,
    val name: String,

    val amount: Int,
    val averagePrice: Double,
    val lastPrice: Double,

    val totalValue: Double,
    val profit: Double,
    val profitPercent: Double,

    val logoUrl: String,
    val backgroundColor: String,
    val textColor: String
)