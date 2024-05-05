package ru.alex0d.investapp.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.alex0d.investapp.data.remote.PortfolioApi
import ru.alex0d.investapp.domain.models.PortfolioInfo

class PortfolioRepository(private val portfolioApi: PortfolioApi) {
    private val tag = PortfolioRepository::class.java.simpleName

    suspend fun getPortfolio(): PortfolioInfo {
        return withContext(Dispatchers.IO) {
            try {
                val portfolio = portfolioApi.getPortfolio()
                Log.d(tag, "Got portfolio")
                portfolio.toPortfolioInfo()
            } catch (e: Exception) {
                // TODO: handle error
                Log.e(tag, "Error getting portfolio", e)
                PortfolioInfo(0.0, 0.0, 0.0, emptyList())
            }
        }
    }
}