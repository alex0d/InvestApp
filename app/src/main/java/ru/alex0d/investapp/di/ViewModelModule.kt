package ru.alex0d.investapp.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.alex0d.investapp.screens.portfolio.PortfolioViewModel

val viewModelModule = module {
    viewModel { PortfolioViewModel(get()) }
}