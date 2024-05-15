package ru.alex0d.investapp.data.repositories

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alex0d.investapp.data.mappers.toPortfolioInfo
import ru.alex0d.investapp.data.remote.models.BuyStockRequest
import ru.alex0d.investapp.data.remote.models.SellStockRequest
import ru.alex0d.investapp.data.remote.services.PortfolioApiService
import ru.alex0d.investapp.domain.models.PortfolioInfo

class PortfolioRepository(private val portfolioApiService: PortfolioApiService) {
    private val tag = PortfolioRepository::class.java.simpleName

    suspend fun getPortfolio(): PortfolioInfo {
        return withContext(Dispatchers.IO) {
            try {
                val portfolio = portfolioApiService.getPortfolio()
                Log.d(tag, "Got portfolio")
                portfolio.toPortfolioInfo()
            } catch (e: Exception) {
                // TODO: handle error
                Log.e(tag, "Error getting portfolio", e)
                PortfolioInfo(0.0, 0.0, 0.0, emptyList())
            }
        }
    }

    suspend fun buyStock(uid: String, amount: Int): Boolean {
        val request = BuyStockRequest(uid, amount)
        val response = portfolioApiService.buyStock(request)
        return response.isSuccessful && response.body().toString() == "Stock bought"
    }

    suspend fun sellStock(uid: String, amount: Int): Boolean {
        val request = SellStockRequest(uid, amount)
        val response = portfolioApiService.sellStock(request)
        return response.isSuccessful && response.body().toString() == "Stock sold"
    }
}