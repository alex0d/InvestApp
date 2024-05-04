package ru.alex0d.investapp.di

import org.koin.dsl.module
import ru.alex0d.investapp.data.PortfolioRepository

val dataModule = module {
    single { PortfolioRepository(get()) }
}