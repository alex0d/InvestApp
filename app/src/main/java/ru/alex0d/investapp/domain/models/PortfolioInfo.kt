package ru.alex0d.investapp.domain.models

data class PortfolioInfo(
    var totalValue: Double,
    var totalProfit: Double,
    var totalProfitPercent: Double,
    var stocks: List<PortfolioStockInfo>
)

data class PortfolioStockInfo(
    var uid: String,
    var ticker: String,
    var name: String,

    var amount: Int,
    var averagePrice: Double,
    var lastPrice: Double,

    var totalValue: Double,
    var profit: Double,
    var profitPercent: Double,

    var logoUrl: String,
    var backgroundColor: String,
    var textColor: String
)