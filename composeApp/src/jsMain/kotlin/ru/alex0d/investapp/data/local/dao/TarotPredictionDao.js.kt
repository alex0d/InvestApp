package ru.alex0d.investapp.data.local.dao

import ru.alex0d.investapp.data.local.models.TarotPredictionDbo

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual interface TarotPredictionDao {
    actual suspend fun getByStockName(stockName: String): TarotPredictionDbo?
    actual suspend fun insert(tarotPredictionDbo: TarotPredictionDbo)
    actual suspend fun delete(tarotPredictionDbo: TarotPredictionDbo)
    actual suspend fun delete(stockName: String)
    actual suspend fun deleteAll()
}