@file:Suppress("unused")

package ru.alex0d.investapp.aurora

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.alex0d.investapp.data.repositories.PortfolioRepository
import ru.alex0d.investapp.utils.AuroraExport
import ru.alex0d.investapp.utils.extensions.promiseWithEvent

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@AuroraExport
class PortfolioRepositoryJs : KoinComponent {
    private val repository by inject<PortfolioRepository>()

    fun getPortfolio() = GlobalScope.promiseWithEvent {
        repository.getPortfolio()
    }

    fun buyStock(uid: String, amount: Int) = GlobalScope.promiseWithEvent {
        repository.buyStock(uid, amount)
    }

    fun sellStock(uid: String, amount: Int) = GlobalScope.promiseWithEvent {
        repository.sellStock(uid, amount)
    }
}

@OptIn(ExperimentalJsExport::class)
@AuroraExport
val portfolioRepository by lazy { PortfolioRepositoryJs() }