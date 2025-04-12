package ru.alex0d.investapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.alex0d.investapp.data.local.AppDatabase
import ru.alex0d.investapp.screens.stock.chart.ChartAdapterFactory
import ru.alex0d.investapp.utils.IO

private fun getRoomDatabase(
    builder: RoomDatabase.Builder<AppDatabase>
): AppDatabase {
    return builder
        .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

fun makeNonJsModule(
    appDatabaseBuilder: NativeInjectionFactory<RoomDatabase.Builder<AppDatabase>>,
    dataStore: NativeInjectionFactory<DataStore<Preferences>>,
): Module {
    return module {
        single { appDatabaseBuilder() }
        single { getRoomDatabase(builder = get()) }
        single { dataStore() }
        single { ChartAdapterFactory() }
    }
}