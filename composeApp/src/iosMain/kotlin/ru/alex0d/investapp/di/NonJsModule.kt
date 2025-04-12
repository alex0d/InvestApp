package ru.alex0d.investapp.di

import ru.alex0d.investapp.data.local.createDataStore
import ru.alex0d.investapp.data.local.getDatabaseBuilder

val nonJsModule = makeNonJsModule(
    appDatabaseBuilder = {
        getDatabaseBuilder()
    },
    dataStore = {
        createDataStore()
    },
)