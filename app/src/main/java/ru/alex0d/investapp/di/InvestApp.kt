package ru.alex0d.investapp.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class InvestApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@InvestApp)
            androidLogger()
            modules(viewModelModule, networkModule, dataModule)
        }
    }
}