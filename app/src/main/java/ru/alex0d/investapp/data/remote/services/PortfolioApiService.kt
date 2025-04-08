package ru.alex0d.investapp.data.remote.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.alex0d.investapp.data.remote.models.BuyStockRequest
import ru.alex0d.investapp.data.remote.models.PortfolioInfoDto
import ru.alex0d.investapp.data.remote.models.SellStockRequest

class PortfolioApiService(
    private val httpClient: HttpClient,
    private val investApiBaseUrl: String
) {
    suspend fun getPortfolio(): PortfolioInfoDto {
        return httpClient.get("$investApiBaseUrl/api/portfolio").body()
    }

    suspend fun buyStock(request: BuyStockRequest): String {
        return httpClient.post("$investApiBaseUrl/api/portfolio/buy") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun sellStock(request: SellStockRequest): String {
        return httpClient.post("$investApiBaseUrl/api/portfolio/sell") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}