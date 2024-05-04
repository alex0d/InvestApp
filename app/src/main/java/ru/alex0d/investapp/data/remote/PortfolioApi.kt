package ru.alex0d.investapp.data.remote

import retrofit2.http.GET
import ru.alex0d.investapp.data.remote.models.PortfolioInfoDto

interface PortfolioApi {
    @GET("/api/portfolio")
    suspend fun getPortfolio(): PortfolioInfoDto
}