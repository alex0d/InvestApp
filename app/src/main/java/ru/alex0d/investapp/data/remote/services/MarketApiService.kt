package ru.alex0d.investapp.data.remote.services

import retrofit2.http.Body
import retrofit2.http.POST
import ru.alex0d.investapp.data.remote.models.CandleDto
import ru.alex0d.investapp.data.remote.models.CandleRequest

interface MarketApiService {
    @POST("/api/market/candles")
    suspend fun getCandles(@Body request: CandleRequest): List<CandleDto>
}