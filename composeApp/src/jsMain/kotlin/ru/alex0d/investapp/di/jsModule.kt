package ru.alex0d.investapp.di

import org.koin.dsl.module
import ru.alex0d.investapp.data.local.AppDatabase
import ru.alex0d.investapp.data.local.AppDatabaseImpl
import ru.alex0d.investapp.screens.stock.chart.ChartAdapterFactory

val jsModule = module {
    single<AppDatabase> { AppDatabaseImpl() }
    single { ChartAdapterFactory() }
}