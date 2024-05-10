package ru.alex0d.investapp.data.remote.services

import retrofit2.http.GET
import retrofit2.http.Path
import ru.alex0d.investapp.data.remote.models.TarotPredictionDto

interface TarotApiService {
    @GET("/api/tarot/{stockName}")
    suspend fun getPredictionByStockName(@Path("stockName") stockName: String): TarotPredictionDto
}