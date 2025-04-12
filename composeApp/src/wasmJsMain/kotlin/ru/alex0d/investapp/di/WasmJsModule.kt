package ru.alex0d.investapp.di

import org.koin.dsl.module
import ru.alex0d.investapp.data.local.AppDatabase
import ru.alex0d.investapp.data.local.AppDatabaseImpl

val wasmJsModule = module {
    single<AppDatabase> { AppDatabaseImpl() }
}