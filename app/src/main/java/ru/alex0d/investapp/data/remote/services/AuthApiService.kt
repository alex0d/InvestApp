package ru.alex0d.investapp.data.remote.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.alex0d.investapp.data.remote.models.AuthRequest
import ru.alex0d.investapp.data.remote.models.AuthResponse
import ru.alex0d.investapp.data.remote.models.RefreshRequest
import ru.alex0d.investapp.data.remote.models.RegisterRequest

class AuthApiService(
    private val httpClient: HttpClient,
    private val investApiBaseUrl: String
) {
    suspend fun authenticate(request: AuthRequest): AuthResponse {
        return httpClient.post("$investApiBaseUrl/api/auth/authenticate") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun register(request: RegisterRequest): AuthResponse {
        return httpClient.post("$investApiBaseUrl/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun refresh(request: RefreshRequest): AuthResponse {
        return httpClient.post("$investApiBaseUrl/api/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}