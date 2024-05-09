package ru.alex0d.investapp.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.alex0d.investapp.screens.login.AuthViewModel
import ru.alex0d.investapp.screens.portfolio.PortfolioViewModel
import ru.alex0d.investapp.screens.profile.ProfileViewModel

val viewModelModule = module {
    viewModel { PortfolioViewModel(portfolioRepository = get()) }

    viewModel {
        AuthViewModel(authRepository = get(), jwtDataStore = get())
    }

    viewModel { ProfileViewModel(jwtDataStore = get()) }
}