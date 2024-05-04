package ru.alex0d.investapp.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alex0d.investapp.data.remote.PortfolioApi
import ru.alex0d.investapp.data.remote.models.PortfolioInfoDto

class PortfolioRepository(private val portfolioApi: PortfolioApi) {
    suspend fun getPortfolio(): PortfolioInfoDto {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("PortfolioRepository", "Getting portfolio")
                val portfolio = portfolioApi.getPortfolio()
                Log.d("PortfolioRepository", "Got portfolio: $portfolio")
                portfolio
            } catch (e: Exception) {
                // TODO: handle error
                Log.e("PortfolioRepository", "Error getting portfolio", e)
                PortfolioInfoDto(0.0, 0.0, 0.0, emptyList())
            }
        }
    }
}