package ru.alex0d.investapp.di

import org.koin.android.ext.koin.androidContext
import ru.alex0d.investapp.data.local.createDataStore
import ru.alex0d.investapp.data.local.getDatabaseBuilder

val nativeModule = makeNativeModule(
    appDatabaseBuilder = {
        getDatabaseBuilder(ctx = androidContext())
    },
    datastore = {
        createDataStore(context = androidContext())
    },
)