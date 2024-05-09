package ru.alex0d.investapp.data.remote.services

import retrofit2.http.Body
import retrofit2.http.POST
import ru.alex0d.investapp.data.remote.models.AuthRequest
import ru.alex0d.investapp.data.remote.models.AuthResponse
import ru.alex0d.investapp.data.remote.models.RefreshRequest
import ru.alex0d.investapp.data.remote.models.RegisterRequest

interface AuthApiService {
    @POST("/api/auth/authenticate")
    suspend fun authenticate(@Body request: AuthRequest): AuthResponse

    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("/api/auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): AuthResponse
}