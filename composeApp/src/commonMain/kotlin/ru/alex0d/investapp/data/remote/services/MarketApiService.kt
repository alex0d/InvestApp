package ru.alex0d.investapp.data.remote.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.alex0d.investapp.data.remote.models.CandleDto
import ru.alex0d.investapp.data.remote.models.CandleRequest

class MarketApiService(
    private val httpClient: HttpClient,
    private val investApiBaseUrl: String
) {
    suspend fun getCandles(request: CandleRequest): List<CandleDto> {
        return httpClient.post("$investApiBaseUrl/api/market/candles") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}