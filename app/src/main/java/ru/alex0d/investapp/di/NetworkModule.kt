package ru.alex0d.investapp.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import ru.alex0d.investapp.BuildConfig
import ru.alex0d.investapp.data.remote.PortfolioApi
import ru.alex0d.investapp.utils.InvestApiTokenInterceptor

const val investApiBaseUrl = BuildConfig.INVEST_API_BASE_URL
const val investApiToken = BuildConfig.INVEST_API_TOKEN

private fun provideHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        )
        .addInterceptor(InvestApiTokenInterceptor(investApiToken))
        .build()
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(investApiBaseUrl)
        .client(okHttpClient)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
}

private fun providePortfolioService(retrofit: Retrofit): PortfolioApi {
    return retrofit.create(PortfolioApi::class.java)
}

val networkModule = module {
    single { provideHttpClient() }
    single { provideRetrofit(get()) }
    single { providePortfolioService(get()) }
}
