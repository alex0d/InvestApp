package ru.alex0d.investapp.utils

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.alex0d.investapp.BuildConfig
import ru.alex0d.investapp.data.local.JwtDataStore
import ru.alex0d.investapp.data.remote.models.AuthResponse
import ru.alex0d.investapp.data.remote.models.RefreshRequest
import ru.alex0d.investapp.data.remote.services.AuthApiService

const val investApiBaseUrl = BuildConfig.INVEST_API_BASE_URL

class AuthAuthenticator(
    private val jwtDataStore: JwtDataStore
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val token = runBlocking {
            jwtDataStore.refreshToken.first()
        } ?: return null

        return runBlocking {
            val newToken = try {
                getNewToken(token)
            } catch (e: Exception) {
                return@runBlocking null
            }

            newToken.let {
                jwtDataStore.saveAccessToken(it.accessToken)
                jwtDataStore.saveRefreshToken(it.refreshToken)

                response.request.newBuilder()
                    .header("Authorization", "Bearer ${it.accessToken}")
                    .build()
            }
        }
    }

    private suspend fun getNewToken(refreshToken: String): AuthResponse {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(investApiBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()

        val service = retrofit.create(AuthApiService::class.java)

        return service.refresh(RefreshRequest(refreshToken)).body()!!
    }
}