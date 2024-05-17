package ru.alex0d.investapp.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.alex0d.investapp.data.local.AppDatabase
import ru.alex0d.investapp.data.local.JwtDataStore
import ru.alex0d.investapp.data.repositories.AuthRepository
import ru.alex0d.investapp.data.repositories.MarketRepository
import ru.alex0d.investapp.data.repositories.PortfolioRepository
import ru.alex0d.investapp.data.repositories.StockRepository
import ru.alex0d.investapp.data.repositories.TarotRepository

fun provideAppDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    ).build()
}

val dataModule = module {
    single { PortfolioRepository(portfolioApiService = get()) }

    single { StockRepository(stockApiService = get())}

    single { MarketRepository(marketApiService = get()) }

    single { AuthRepository(apiService = get()) }

    single { JwtDataStore(context = androidContext()) }

    single { provideAppDatabase(androidContext()) }

    single { TarotRepository(tarotApiService = get(), database = get()) }
}