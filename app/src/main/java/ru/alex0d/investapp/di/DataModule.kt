package ru.alex0d.investapp.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.alex0d.investapp.data.JwtDataStore
import ru.alex0d.investapp.data.repositories.AuthRepository
import ru.alex0d.investapp.data.repositories.PortfolioRepository
import ru.alex0d.investapp.data.repositories.StockRepository

val dataModule = module {
    single { PortfolioRepository(portfolioApiService = get()) }

    single { StockRepository(stockApiService = get())}

    single { AuthRepository(apiService = get()) }

    single { JwtDataStore(context = androidContext()) }
}