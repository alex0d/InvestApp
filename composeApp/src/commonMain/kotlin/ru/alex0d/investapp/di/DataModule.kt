package ru.alex0d.investapp.di

import org.koin.dsl.module
import ru.alex0d.investapp.data.repositories.MarketRepository
import ru.alex0d.investapp.data.repositories.PortfolioRepository
import ru.alex0d.investapp.data.repositories.StockRepository
import ru.alex0d.investapp.data.repositories.TarotRepository
import ru.alex0d.investapp.data.repositories.UserRepository

val dataModule = module {
    single { PortfolioRepository(portfolioApiService = get()) }

    single { StockRepository(stockApiService = get())}

    single { MarketRepository(marketApiService = get()) }

    single { UserRepository(apiService = get(), userDataStore = get()) }

    single { TarotRepository(tarotApiService = get(), database = get()) }
}