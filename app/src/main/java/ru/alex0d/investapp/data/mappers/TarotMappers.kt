package ru.alex0d.investapp.data.mappers

import ru.alex0d.investapp.data.local.models.TarotPredictionDbo
import ru.alex0d.investapp.data.remote.models.TarotPredictionDto
import ru.alex0d.investapp.domain.models.TarotCard
import ru.alex0d.investapp.domain.models.TarotPrediction

fun TarotPredictionDto.toTarotPredictionDbo(stockName: String) = TarotPredictionDbo(
    stockName = stockName,
    cardName = cardName,
    prediction = prediction
)

fun TarotPredictionDto.toTarotPrediction() = TarotPrediction(
    card = TarotCard.valueOf(cardName.uppercase()),
    prediction = prediction
)

fun TarotPredictionDbo.toTarotPrediction() = TarotPrediction(
    card = TarotCard.valueOf(cardName.uppercase()),
    prediction = prediction
)