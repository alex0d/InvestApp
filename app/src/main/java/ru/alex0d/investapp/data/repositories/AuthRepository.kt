package ru.alex0d.investapp.data.repositories

import kotlinx.coroutines.flow.first
import ru.alex0d.investapp.data.local.UserDataStore
import ru.alex0d.investapp.data.remote.models.AuthRequest
import ru.alex0d.investapp.data.remote.models.RegisterRequest
import ru.alex0d.investapp.data.remote.services.AuthApiService
import ru.alex0d.investapp.domain.models.AuthResult

class AuthRepository(
    private val apiService: AuthApiService,
    private val userDataStore: UserDataStore
) {

    suspend fun register(firstname: String, lastname: String?, email: String, password: String): AuthResult {
        val request = RegisterRequest(
            firstname = firstname,
            lastname = lastname,
            email = email,
            password = password
        )

        val response = try {
            apiService.register(request)
        } catch (e: Exception) {
            return AuthResult.UNKNOWN_ERROR
        }

        return if (response.isSuccessful && response.body() != null) {
            userDataStore.saveUserDetails(
                accessToken = response.body()!!.accessToken,
                refreshToken = response.body()!!.refreshToken,
                firstname = response.body()!!.firstname,
                lastname = response.body()!!.lastname,
                email = response.body()!!.email
            )
            AuthResult.SUCCESS
        } else if (response.code() == 409) {
            AuthResult.EMAIL_ALREADY_REGISTERED
        } else {
            AuthResult.UNKNOWN_ERROR
        }
    }

    suspend fun authenticate(email: String, password: String): AuthResult {
        val request = AuthRequest(
            email = email,
            password = password
        )

        val response = try {
            apiService.authenticate(request)
        } catch (e: Exception) {
            return AuthResult.UNKNOWN_ERROR
        }

        return if (response.isSuccessful && response.body() != null) {
            userDataStore.saveUserDetails(
                accessToken = response.body()!!.accessToken,
                refreshToken = response.body()!!.refreshToken,
                firstname = response.body()!!.firstname,
                lastname = response.body()!!.lastname,
                email = response.body()!!.email
            )
            AuthResult.SUCCESS
        } else if (response.code() == 401) {
            AuthResult.INVALID_CREDENTIALS
        } else if (response.code() == 422) {
            AuthResult.USER_NOT_FOUND
        } else {
            AuthResult.UNKNOWN_ERROR
        }
    }

    suspend fun authenticateByTokensInDataBase(): AuthResult {
        val accessToken = userDataStore.accessToken.first()
        val refreshToken = userDataStore.refreshToken.first()

        return if (accessToken != null && refreshToken != null) {
            AuthResult.SUCCESS
        } else {
            AuthResult.INVALID_CREDENTIALS
        }
    }

    suspend fun logout() {
        userDataStore.clear()
    }

    suspend fun getUserFirstname(): String? {
        return userDataStore.userFirstname.first()
    }

    suspend fun getUserLastname(): String? {
        return userDataStore.userLastname.first()
    }

    suspend fun getUserEmail(): String? {
        return userDataStore.userEmail.first()
    }
}