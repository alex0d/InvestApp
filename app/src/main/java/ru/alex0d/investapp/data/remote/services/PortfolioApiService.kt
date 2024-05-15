package ru.alex0d.investapp.data.remote.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.alex0d.investapp.data.remote.models.BuyStockRequest
import ru.alex0d.investapp.data.remote.models.PortfolioInfoDto
import ru.alex0d.investapp.data.remote.models.SellStockRequest

interface PortfolioApiService {
    @GET("/api/portfolio")
    suspend fun getPortfolio(): PortfolioInfoDto

    @POST("/api/portfolio/buy")
    suspend fun buyStock(@Body request: BuyStockRequest): Response<String>

    @POST("/api/portfolio/sell")
    suspend fun sellStock(@Body request: SellStockRequest): Response<String>
}