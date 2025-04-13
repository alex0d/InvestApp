package ru.alex0d.investapp.data.local.dao

import ru.alex0d.investapp.data.local.models.TarotPredictionDbo

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect interface TarotPredictionDao {
    suspend fun getByStockName(stockName: String): TarotPredictionDbo?

    suspend fun insert(tarotPredictionDbo: TarotPredictionDbo)

    suspend fun delete(tarotPredictionDbo: TarotPredictionDbo)

    suspend fun delete(stockName: String)

    suspend fun deleteAll()
}