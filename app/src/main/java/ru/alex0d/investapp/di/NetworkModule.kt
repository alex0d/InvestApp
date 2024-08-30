package ru.alex0d.investapp.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.alex0d.investapp.BuildConfig
import ru.alex0d.investapp.data.remote.services.AuthApiService
import ru.alex0d.investapp.data.remote.services.MarketApiService
import ru.alex0d.investapp.data.remote.services.PortfolioApiService
import ru.alex0d.investapp.data.remote.services.StockApiService
import ru.alex0d.investapp.data.remote.services.TarotApiService
import ru.alex0d.investapp.utils.AuthAuthenticator
import ru.alex0d.investapp.utils.AuthInterceptor
import ru.alex0d.investapp.utils.connectivity.ConnectivityObserver
import ru.alex0d.investapp.utils.connectivity.NetworkConnectivityObserver
import java.util.concurrent.TimeUnit

const val investApiBaseUrl = BuildConfig.INVEST_API_BASE_URL

private fun provideNetworkConnectivityObserver(
    context: Context
): ConnectivityObserver {
    return NetworkConnectivityObserver(context)
}

private fun provideHttpClient(
    authInterceptor: AuthInterceptor,
    authAuthenticator: AuthAuthenticator
): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        )
        .addInterceptor(authInterceptor)
        .authenticator(authAuthenticator)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(investApiBaseUrl)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
}

private fun providePortfolioService(retrofit: Retrofit): PortfolioApiService {
    return retrofit.create(PortfolioApiService::class.java)
}

private fun provideStockService(retrofit: Retrofit): StockApiService {
    return retrofit.create(StockApiService::class.java)
}

private fun provideMarketService(retrofit: Retrofit): MarketApiService {
    return retrofit.create(MarketApiService::class.java)
}

private fun provideTarotService(retrofit: Retrofit): TarotApiService {
    return retrofit.create(TarotApiService::class.java)
}

private fun provideAuthService(retrofit: Retrofit): AuthApiService {
    return retrofit.create(AuthApiService::class.java)
}

val networkModule = module {
    single { provideNetworkConnectivityObserver(context = get()) }

    single { AuthInterceptor(userDataStore = get()) }

    single { AuthAuthenticator(userDataStore = get()) }

    single { provideHttpClient(authInterceptor = get(), authAuthenticator = get()) }

    single { provideRetrofit(okHttpClient = get()) }

    single { providePortfolioService(retrofit = get()) }

    single { provideStockService(retrofit = get()) }

    single { provideMarketService(retrofit = get()) }

    single { provideTarotService(retrofit = get()) }

    single { provideAuthService(retrofit = get()) }
}
