package ru.alex0d.investapp.data.remote.services

import retrofit2.http.GET
import retrofit2.http.Path
import ru.alex0d.investapp.data.remote.models.ShareDto

interface StockApiService {
    @GET("/api/search/shares/{ticker}")
    suspend fun getSharesByTicker(@Path("ticker") ticker: String): List<ShareDto>

    @GET("/api/share/{uid}")
    suspend fun getShareByUid(@Path("uid") uid: String): ShareDto
}