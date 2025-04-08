package ru.alex0d.investapp.di

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import ru.alex0d.investapp.BuildConfig
import ru.alex0d.investapp.data.local.UserDataStore
import ru.alex0d.investapp.data.remote.models.AuthResponse
import ru.alex0d.investapp.data.remote.models.RefreshRequest
import ru.alex0d.investapp.data.remote.services.AuthApiService
import ru.alex0d.investapp.data.remote.services.MarketApiService
import ru.alex0d.investapp.data.remote.services.PortfolioApiService
import ru.alex0d.investapp.data.remote.services.StockApiService
import ru.alex0d.investapp.data.remote.services.TarotApiService
import ru.alex0d.investapp.utils.connectivity.ConnectivityObserver
import ru.alex0d.investapp.utils.connectivity.NetworkConnectivityObserver

const val investApiBaseUrl = BuildConfig.INVEST_API_BASE_URL

private fun provideNetworkConnectivityObserver(
    context: Context
): ConnectivityObserver {
    return NetworkConnectivityObserver(context)
}

fun provideHttpClient(userDataSource: UserDataStore): HttpClient { // TODO: change UserDataStore to multiplatform solution
    return HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(
                        accessToken = userDataSource.accessToken.first() ?: "",
                        refreshToken = userDataSource.refreshToken.first() ?: ""
                    )
                }

                refreshTokens {
                    val refreshRequest = RefreshRequest(
                        refreshToken = userDataSource.refreshToken.first() ?: ""
                    )

                    client.post("$investApiBaseUrl/api/auth/refresh") {
                        markAsRefreshTokenRequest()
                        contentType(ContentType.Application.Json)
                        setBody(refreshRequest)
                    }.body<AuthResponse>().let {
                        userDataSource.saveAccessToken(it.accessToken)
                        userDataSource.saveRefreshToken(it.refreshToken)

                        BearerTokens(
                            accessToken = it.accessToken,
                            refreshToken = it.refreshToken
                        )
                    }
                }
            }
        }

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }

        install(HttpTimeout) {
            socketTimeoutMillis = 60_000
        }
    }
}

val networkModule = module {
    single { provideNetworkConnectivityObserver(context = get()) }

    single { provideHttpClient(userDataSource = get()) }

    single { AuthApiService(httpClient = get(), investApiBaseUrl = investApiBaseUrl) }

    single { MarketApiService(httpClient = get(), investApiBaseUrl = investApiBaseUrl) }

    single { PortfolioApiService(httpClient = get(), investApiBaseUrl = investApiBaseUrl) }

    single { StockApiService(httpClient = get(), investApiBaseUrl = investApiBaseUrl) }

    single { TarotApiService(httpClient = get(), investApiBaseUrl = investApiBaseUrl) }
}
