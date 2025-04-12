package ru.alex0d.investapp.data.local.models

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual data class TarotPredictionDbo actual constructor(
    actual val stockName: String,
    actual val cardName: String,
    actual val prediction: String
)