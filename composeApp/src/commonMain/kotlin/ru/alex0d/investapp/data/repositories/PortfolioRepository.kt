package ru.alex0d.investapp.data.repositories

import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alex0d.investapp.data.mappers.toPortfolioInfo
import ru.alex0d.investapp.data.remote.models.BuyStockRequest
import ru.alex0d.investapp.data.remote.models.SellStockRequest
import ru.alex0d.investapp.data.remote.services.PortfolioApiService
import ru.alex0d.investapp.domain.models.PortfolioInfo
import ru.alex0d.investapp.utils.IO

class PortfolioRepository(private val portfolioApiService: PortfolioApiService) {

    suspend fun getPortfolio(): PortfolioInfo? {
        return withContext(Dispatchers.IO) {
            try {
                val portfolio = portfolioApiService.getPortfolio()
                Napier.d("Got portfolio")
                portfolio.toPortfolioInfo()
            } catch (e: Exception) {
                Napier.e("Error getting portfolio", e)
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