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

    suspend fun getPortfolio(): PortfolioInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val portfolio = portfolioApiService.getPortfolio()
                Log.d(tag, "Got portfolio")
                portfolio.toPortfolioInfo()
            } catch (e: Exception) {
                Log.e(tag, "Error getting portfolio", e)
                null
            }
        }
    }

    suspend fun buyStock(uid: String, amount: Int): Boolean {
        val request = BuyStockRequest(uid, amount)

        val response = try {
            portfolioApiService.buyStock(request)
        } catch (e: Exception) {
            return false
        }

        return response == "Stock bought"
    }

    suspend fun sellStock(uid: String, amount: Int): Boolean {
        val request = SellStockRequest(uid, amount)

        val response = try {
            portfolioApiService.sellStock(request)
        } catch (e: Exception) {
            return false
        }

        return response == "Stock sold"
    }
}