package ru.alex0d.investapp.data.remote.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.alex0d.investapp.data.remote.models.TarotPredictionDto

class TarotApiService(
    private val httpClient: HttpClient,
    private val investApiBaseUrl: String
) {
    suspend fun getPredictionByStockName(stockName: String): TarotPredictionDto {
        return httpClient.get("$investApiBaseUrl/api/tarot/$stockName").body()
    }
}