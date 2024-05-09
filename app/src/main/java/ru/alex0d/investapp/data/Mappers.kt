package ru.alex0d.investapp.data

import ru.alex0d.investapp.data.remote.models.PortfolioInfoDto
import ru.alex0d.investapp.data.remote.models.PortfolioStockInfoDto
import ru.alex0d.investapp.domain.models.PortfolioInfo
import ru.alex0d.investapp.domain.models.PortfolioStockInfo

fun PortfolioInfoDto.toPortfolioInfo() = PortfolioInfo(
    totalValue = totalValue,
    totalProfit = totalProfit,
    totalProfitPercent = totalProfitPercent,
    stocks = stocks.map { it.toPortfolioStockInfo() }
)

fun PortfolioStockInfoDto.toPortfolioStockInfo() = PortfolioStockInfo(
    uid = uid,
    ticker = ticker,
    name = name,
    amount = amount,
    price = price,
    totalValue = totalValue,
    profit = profit,
    profitPercent = profitPercent,
    logoUrl = logoUrl,
    backgroundColor = backgroundColor,
    textColor = textColor
)