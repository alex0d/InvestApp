package ru.alex0d.investapp.data.repositories

import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alex0d.investapp.data.mappers.toShare
import ru.alex0d.investapp.data.remote.services.StockApiService
import ru.alex0d.investapp.domain.models.Share
import ru.alex0d.investapp.utils.IO

class StockRepository(private val stockApiService: StockApiService) {

    suspend fun getSharesByTicker(ticker: String): List<Share>? {
        return withContext(Dispatchers.IO) {
            try {
                val shares = stockApiService.getSharesByTicker(ticker)
                Napier.d("Got shares by ticker")
                shares.map { it.toShare() }
            } catch (e: Exception) {
                Napier.e("Error getting shares by ticker $ticker", e)
                null
            }
        }
    }

    suspend fun getShareByUid(uid: String): Share? {
        return withContext(Dispatchers.IO) {
            try {
                val share = stockApiService.getShareByUid(uid)
                Napier.d("Got share by uid")
                share.toShare()
            } catch (e: Exception) {
                Napier.e("Error getting share by uid $uid", e)
                null
            }
        }
    }
}
