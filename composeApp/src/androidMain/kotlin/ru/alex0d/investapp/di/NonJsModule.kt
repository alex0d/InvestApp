package ru.alex0d.investapp.di

import org.koin.android.ext.koin.androidContext
import ru.alex0d.investapp.data.local.createDataStore
import ru.alex0d.investapp.data.local.getDatabaseBuilder

val nonJsModule = makeNonJsModule(
    appDatabaseBuilder = {
        getDatabaseBuilder(ctx = androidContext())
    },
    dataStore = {
        createDataStore(context = androidContext())
    },
)