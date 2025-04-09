package ru.alex0d.investapp.data.remote.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.alex0d.investapp.data.remote.models.ShareDto

class StockApiService(
    private val httpClient: HttpClient,
    private val investApiBaseUrl: String
) {
    suspend fun getSharesByTicker(ticker: String): List<ShareDto> {
        return httpClient.get("$investApiBaseUrl/api/search/shares/$ticker").body()
    }

    suspend fun getShareByUid(uid: String): ShareDto {
        return httpClient.get("$investApiBaseUrl/api/share/$uid").body()
    }
}