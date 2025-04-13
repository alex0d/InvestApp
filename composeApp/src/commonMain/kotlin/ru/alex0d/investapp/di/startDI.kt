package ru.alex0d.investapp.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun startDI(
    vararg platformSpecificModules: Module,
    appDeclaration: KoinAppDeclaration = {}
) {
    startKoin {
        appDeclaration()
        modules(*platformSpecificModules, dataModule, networkModule, viewModelModule)
    }
}