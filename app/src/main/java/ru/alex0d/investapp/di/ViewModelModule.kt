package ru.alex0d.investapp.di

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.alex0d.investapp.screens.auth.AuthViewModel
import ru.alex0d.investapp.screens.order.OrderViewModel
import ru.alex0d.investapp.screens.portfolio.PortfolioViewModel
import ru.alex0d.investapp.screens.profile.ProfileViewModel
import ru.alex0d.investapp.screens.root.RootViewModel
import ru.alex0d.investapp.screens.search.SearchViewModel
import ru.alex0d.investapp.screens.stock.StockDetailsViewModel
import ru.alex0d.investapp.screens.tarot.TarotViewModel

val viewModelModule = module {

    viewModel { RootViewModel(networkConnectivityObserver = get()) }

    viewModel { AuthViewModel(userRepository = get()) }

    viewModel { PortfolioViewModel(portfolioRepository = get()) }

    viewModel { SearchViewModel(stockRepository = get()) }

    viewModel { ProfileViewModel(userRepository = get()) }

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