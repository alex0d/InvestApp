package ru.alex0d.investapp.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.alex0d.investapp.screens.login.AuthViewModel
import ru.alex0d.investapp.screens.order.OrderViewModel
import ru.alex0d.investapp.screens.portfolio.PortfolioViewModel
import ru.alex0d.investapp.screens.profile.ProfileViewModel
import ru.alex0d.investapp.screens.search.SearchViewModel
import ru.alex0d.investapp.screens.stock.StockDetailsViewModel
import ru.alex0d.investapp.screens.tarot.TarotViewModel

val viewModelModule = module {
    viewModel { PortfolioViewModel(portfolioRepository = get()) }

    viewModel {
        AuthViewModel(authRepository = get(), jwtDataStore = get())
    }

    viewModel { SearchViewModel(stockRepository = get()) }

    viewModel { ProfileViewModel(jwtDataStore = get()) }

    viewModel { parameters ->
        StockDetailsViewModel(
            stockRepository = get(),
            marketRepository = get(),
            portfolioRepository = get(),
            stockUid = parameters.get()
        )
    }

    viewModel { parameters ->
        OrderViewModel(
            stockRepository = get(),
            portfolioRepository = get(),
            orderAction = parameters.get(),
            stockUid = parameters.get()
        )
    }

    viewModel { parameters ->
        TarotViewModel(
            tarotRepository = get(),
            stockName = parameters.get()
        )
    }
}