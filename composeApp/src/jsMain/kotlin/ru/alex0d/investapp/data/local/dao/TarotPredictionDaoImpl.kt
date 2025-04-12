package ru.alex0d.investapp.data.local.dao

import ru.alex0d.investapp.data.local.models.TarotPredictionDbo

// TODO: Implement saving data in web target
class TarotPredictionDaoImpl : TarotPredictionDao {
    override suspend fun getByStockName(stockName: String): TarotPredictionDbo? {
        // Means there is no tarot card in the cache
        return null
    }

    override suspend fun insert(tarotPredictionDbo: TarotPredictionDbo) {
        // no-op
    }

    override suspend fun delete(tarotPredictionDbo: TarotPredictionDbo) {
        // no-op
    }

    override suspend fun delete(stockName: String) {
        // no-op
    }

    override suspend fun deleteAll() {
        // no-op
    }

}