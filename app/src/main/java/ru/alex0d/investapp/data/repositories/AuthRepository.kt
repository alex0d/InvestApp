package ru.alex0d.investapp.data.repositories

import ru.alex0d.investapp.data.remote.services.AuthApiService
import ru.alex0d.investapp.data.remote.models.AuthRequest
import ru.alex0d.investapp.data.remote.models.AuthResponse
import ru.alex0d.investapp.data.remote.models.RefreshRequest
import ru.alex0d.investapp.data.remote.models.RegisterRequest

class AuthRepository(private val apiService: AuthApiService) {
    suspend fun register(request: RegisterRequest): AuthResponse {
        return apiService.register(request)
    }

    suspend fun authenticate(request: AuthRequest): AuthResponse {
        return apiService.authenticate(request)
    }

    suspend fun refresh(request: RefreshRequest): AuthResponse {
        return apiService.refresh(request)
    }
}