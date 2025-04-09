package ru.alex0d.investapp.data.repositories

import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alex0d.investapp.data.local.AppDatabase
import ru.alex0d.investapp.data.mappers.toTarotPrediction
import ru.alex0d.investapp.data.mappers.toTarotPredictionDbo
import ru.alex0d.investapp.data.remote.services.TarotApiService
import ru.alex0d.investapp.domain.models.TarotPrediction
import ru.alex0d.investapp.utils.IO

class TarotRepository(
    private val tarotApiService: TarotApiService,
    private val database: AppDatabase
) {

    suspend fun getPrediction(stockName: String): TarotPrediction? {
        return withContext(Dispatchers.IO) {
            val prediction = getFromDatabase(stockName)
            if (prediction != null) {
                Napier.d("Got tarot prediction from database")
                return@withContext prediction.toTarotPrediction()
            }

            Napier.d("Getting tarot prediction from api")
            return@withContext getFromApi(stockName)
        }
    }

    suspend fun refreshPrediction(stockName: String): TarotPrediction? {
        return withContext(Dispatchers.IO) {
            database.tarotPredictionDao().delete(stockName)
            return@withContext getFromApi(stockName)
        }
    }

    private suspend fun getFromApi(stockName: String): TarotPrediction? {
        val prediction =  try {
            tarotApiService.getPredictionByStockName(stockName)
        } catch (e: Exception) {
            Napier.e("Error getting tarot prediction by stock name $stockName", e)
            return null
        }

        database.tarotPredictionDao().insert(
            prediction.toTarotPredictionDbo(stockName)
        )

        return prediction.toTarotPrediction()
    }

    private suspend fun getFromDatabase(stockName: String) =
        database.tarotPredictionDao().getByStockName(stockName)

}