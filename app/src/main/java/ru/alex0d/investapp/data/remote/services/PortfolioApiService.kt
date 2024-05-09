package ru.alex0d.investapp.data.remote.services

import retrofit2.http.GET
import ru.alex0d.investapp.data.remote.models.PortfolioInfoDto

interface PortfolioApiService {
    @GET("/api/portfolio")
    suspend fun getPortfolio(): PortfolioInfoDto
}