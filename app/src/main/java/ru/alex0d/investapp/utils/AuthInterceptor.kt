package ru.alex0d.investapp.utils

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.alex0d.investapp.data.JwtDataStore

class AuthInterceptor(private val jwtDataStore: JwtDataStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = runBlocking { jwtDataStore.accessToken.first() }
        val authenticatedRequest = if (!accessToken.isNullOrEmpty()) {
            request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else {
            request
        }
        return chain.proceed(authenticatedRequest)
    }
}