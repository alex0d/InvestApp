package ru.alex0d.investapp.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.alex0d.investapp.data.local.AppDatabase
import ru.alex0d.investapp.data.local.UserDataStore
import ru.alex0d.investapp.data.repositories.MarketRepository
import ru.alex0d.investapp.data.repositories.PortfolioRepository
import ru.alex0d.investapp.data.repositories.StockRepository
import ru.alex0d.investapp.data.repositories.TarotRepository
import ru.alex0d.investapp.data.repositories.UserRepository

fun provideAppDatabase(context: Context): AppDatabase {
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    ).build()
}

val dataModule = module {
    single { provideAppDatabase(context = androidContext()) }

    single { UserDataStore(context = androidContext()) }

    single { PortfolioRepository(portfolioApiService = get()) }

    single { StockRepository(stockApiService = get())}

    single { MarketRepository(marketApiService = get()) }

    single { UserRepository(apiService = get(), userDataStore = get()) }

    single { TarotRepository(tarotApiService = get(), database = get()) }
}