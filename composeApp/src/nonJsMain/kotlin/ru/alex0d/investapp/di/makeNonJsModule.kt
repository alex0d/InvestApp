package ru.alex0d.investapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.RoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.alex0d.investapp.data.local.AppDatabase

fun makeNonJsModule(
    appDatabaseBuilder: NativeInjectionFactory<RoomDatabase.Builder<AppDatabase>>,
    dataStore: NativeInjectionFactory<DataStore<Preferences>>,
): Module {
    return module {
        single { appDatabaseBuilder() }
        single { dataStore() }
    }
}