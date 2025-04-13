@file:Suppress("unused")

package ru.alex0d.investapp.aurora

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.alex0d.investapp.data.repositories.MarketRepository
import ru.alex0d.investapp.domain.models.CandleInterval
import ru.alex0d.investapp.utils.AuroraExport
import ru.alex0d.investapp.utils.extensions.promiseWithEvent

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@AuroraExport
class MarketRepositoryJs : KoinComponent {
    private val repository by inject<MarketRepository>()

    fun getCandles(uid: String, from: Long, to: Long, interval: CandleInterval) = GlobalScope.promiseWithEvent {
        repository.getCandles(uid, from, to, interval)
    }
}

@OptIn(ExperimentalJsExport::class)
@AuroraExport
val marketRepository by lazy { MarketRepositoryJs() }