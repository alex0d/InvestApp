package ru.alex0d.investapp.utils.http

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.alex0d.investapp.data.local.UserDataStore

class AuthInterceptor(private val userDataStore: UserDataStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = runBlocking { userDataStore.accessToken.first() }
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