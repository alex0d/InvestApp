package ru.alex0d.investapp.data.mappers

import ru.alex0d.investapp.data.remote.models.ShareDto
import ru.alex0d.investapp.domain.models.Share

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
    lot = lot,
    url = url,
    textColor = textColor,
    backgroundColor = backgroundColor
)