package ru.alex0d.investapp.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module
import ru.alex0d.investapp.data.local.AppDatabase
import ru.alex0d.investapp.data.local.UserDataStore
import ru.alex0d.investapp.data.repositories.MarketRepository
import ru.alex0d.investapp.data.repositories.PortfolioRepository
import ru.alex0d.investapp.data.repositories.StockRepository
import ru.alex0d.investapp.data.repositories.TarotRepository
import ru.alex0d.investapp.data.repositories.UserRepository
import ru.alex0d.investapp.utils.IO

private fun provideAppDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .setQueryCoroutineContext(Dispatchers.IO)
        .setDriver(BundledSQLiteDriver())
        .build()
}

val dataModule = module {
    single { provideAppDatabase(builder = get()) }

    single { UserDataStore(dataStore = get()) }

    single { PortfolioRepository(portfolioApiService = get()) }

    single { StockRepository(stockApiService = get())}

    single { MarketRepository(marketApiService = get()) }

    single { UserRepository(apiService = get(), userDataStore = get()) }

    single { TarotRepository(tarotApiService = get(), database = get()) }
}