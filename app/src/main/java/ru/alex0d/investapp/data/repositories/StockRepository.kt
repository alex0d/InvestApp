package ru.alex0d.investapp.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alex0d.investapp.data.mappers.toShare
import ru.alex0d.investapp.data.remote.services.StockApiService
import ru.alex0d.investapp.domain.models.Share

class StockRepository(private val stockApiService: StockApiService) {
    private val tag = StockRepository::class.java.simpleName

    suspend fun getSharesByTicker(ticker: String): List<Share>? {
        return withContext(Dispatchers.IO) {
            try {
                val shares = stockApiService.getSharesByTicker(ticker)
                Log.d(tag, "Got shares by ticker")
                shares.map { it.toShare() }
            } catch (e: Exception) {
                Log.e(tag, "Error getting shares by ticker $ticker", e)
                null
            }
        }
    }

    suspend fun getShareByUid(uid: String): Share? {
        return withContext(Dispatchers.IO) {
            try {
                val share = stockApiService.getShareByUid(uid)
                Log.d(tag, "Got share by uid")
                share.toShare()
            } catch (e: Exception) {
                Log.e(tag, "Error getting share by uid $uid", e)
                null
            }
        }
    }
}
