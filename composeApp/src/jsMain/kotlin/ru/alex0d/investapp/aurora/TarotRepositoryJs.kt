@file:Suppress("unused")

package ru.alex0d.investapp.aurora

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.alex0d.investapp.data.repositories.TarotRepository
import ru.alex0d.investapp.utils.AuroraExport
import ru.alex0d.investapp.utils.extensions.promiseWithEvent

@OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@AuroraExport
class TarotRepositoryJs : KoinComponent {
    private val repository by inject<TarotRepository>()

    fun getPrediction(stockName: String) = GlobalScope.promiseWithEvent {
        repository.getPrediction(stockName)
    }

    fun refreshPrediction(stockName: String) = GlobalScope.promiseWithEvent {
        repository.refreshPrediction(stockName)
    }
}

@OptIn(ExperimentalJsExport::class)
@AuroraExport
val tarotRepository by lazy { TarotRepositoryJs() }