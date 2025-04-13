@file:Suppress("unused")

package ru.alex0d.investapp.aurora

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.alex0d.investapp.data.repositories.StockRepository
import ru.alex0d.investapp.utils.AuroraExport
import ru.alex0d.investapp.utils.extensions.promiseWithEvent

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@AuroraExport
class StockRepositoryJs : KoinComponent {
    private val repository by inject<StockRepository>()

    fun getSharesByTicker(ticker: String) = GlobalScope.promiseWithEvent {
        repository.getSharesByTicker(ticker)
    }

    fun getShareByUid(uid: String) = GlobalScope.promiseWithEvent {
        repository.getShareByUid(uid)
    }
}

@OptIn(ExperimentalJsExport::class)
@AuroraExport
val stockRepository by lazy { StockRepositoryJs() }