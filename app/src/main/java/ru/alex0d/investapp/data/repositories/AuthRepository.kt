package ru.alex0d.investapp.data.repositories

import ru.alex0d.investapp.data.remote.models.AuthRequest
import ru.alex0d.investapp.data.remote.models.RefreshRequest
import ru.alex0d.investapp.data.remote.models.RegisterRequest
import ru.alex0d.investapp.data.remote.services.AuthApiService
import ru.alex0d.investapp.domain.models.AuthData
import ru.alex0d.investapp.domain.models.AuthResult

class AuthRepository(private val apiService: AuthApiService) {

    suspend fun register(firstname: String, lastname: String, email: String, password: String): AuthData {
        val request = RegisterRequest(
            firstname = firstname,
            lastname = lastname,
            email = email,
            password = password
        )

        val response = apiService.register(request)

        return if (response.isSuccessful && response.body() != null) {
            AuthData(
                result = AuthResult.SUCCESS,
                accessToken = response.body()!!.accessToken,
                refreshToken = response.body()!!.refreshToken
            )
        } else if (response.code() == 409) {
            AuthData(result = AuthResult.EMAIL_ALREADY_REGISTERED)
        } else {
            AuthData(result = AuthResult.UNKNOWN_ERROR)
        }
    }

    suspend fun authenticate(email: String, password: String): AuthData {
        val request = AuthRequest(
            email = email,
            password = password
        )

        val response = apiService.authenticate(request)

        return if (response.isSuccessful && response.body() != null) {
            AuthData(
                result = AuthResult.SUCCESS,
                accessToken = response.body()!!.accessToken,
                refreshToken = response.body()!!.refreshToken
            )
        } else if (response.code() == 401) {
            AuthData(result = AuthResult.INVALID_CREDENTIALS)
        } else if (response.code() == 422) {
            AuthData(result = AuthResult.USER_NOT_FOUND)
        } else {
            AuthData(result = AuthResult.UNKNOWN_ERROR)
        }
    }

    suspend fun refresh(request: RefreshRequest): AuthData {
        val response = apiService.refresh(request)

        return if (response.isSuccessful && response.body() != null) {
            AuthData(
                result = AuthResult.SUCCESS,
                accessToken = response.body()!!.accessToken,
                refreshToken = response.body()!!.refreshToken
            )
        } else {
            AuthData(result = AuthResult.UNKNOWN_ERROR)
        }
    }
}