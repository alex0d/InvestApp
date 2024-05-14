package ru.alex0d.investapp.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alex0d.investapp.data.mappers.toCandle
import ru.alex0d.investapp.data.remote.models.CandleRequest
import ru.alex0d.investapp.data.remote.services.MarketApiService
import ru.alex0d.investapp.domain.models.Candle
import ru.alex0d.investapp.domain.models.CandleInterval
import ru.alex0d.investapp.domain.models.toApiString

class MarketRepository(private val marketApiService: MarketApiService) {
    private val tag = MarketRepository::class.java.simpleName

    suspend fun getCandles(uid: String, from: Long, to: Long, interval: CandleInterval): List<Candle>? {
        return withContext(Dispatchers.IO) {
            try {
                val request = CandleRequest(
                    uid = uid,
                    from = from,
                    to = to,
                    interval = interval.toApiString()
                )
                val candles = marketApiService.getCandles(request)
                Log.d(tag, "Got candles")
                candles.map { it.toCandle() }
            } catch (e: Exception) {
                Log.e(tag, "Error getting portfolio", e)
                null
            }
        }
    }
}