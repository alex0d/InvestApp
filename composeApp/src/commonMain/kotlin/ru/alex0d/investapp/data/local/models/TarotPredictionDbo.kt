@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package ru.alex0d.investapp.data.local.models

expect class TarotPredictionDbo(
    stockName: String,
    cardName: String,
    prediction: String
) {
    val stockName: String
    val cardName: String
    val prediction: String
}