package ru.alex0d.investapp.data

import ru.alex0d.investapp.data.remote.models.PortfolioInfoDto
import ru.alex0d.investapp.data.remote.models.PortfolioStockInfoDto
import ru.alex0d.investapp.data.remote.models.ShareDto
import ru.alex0d.investapp.domain.models.PortfolioInfo
import ru.alex0d.investapp.domain.models.PortfolioStockInfo
import ru.alex0d.investapp.domain.models.Share

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
    averagePrice = averagePrice,
    lastPrice = lastPrice,
    totalValue = totalValue,
    profit = profit,
    profitPercent = profitPercent,
    logoUrl = logoUrl,
    backgroundColor = backgroundColor,
    textColor = textColor
)

fun ShareDto.toShare() = Share(
    uid = uid,
    figi = figi,
    ticker = ticker,
    classCode = classCode,
    isin = isin,
    currency = currency,
    name = name,
    countryOfRisk = countryOfRisk,
    countryOfRiskName = countryOfRiskName,
    sector = sector,
    lastPrice = lastPrice,
    url = url,
    textColor = textColor,
    backgroundColor = backgroundColor
)