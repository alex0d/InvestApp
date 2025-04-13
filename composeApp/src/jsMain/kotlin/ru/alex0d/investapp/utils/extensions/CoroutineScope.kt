package ru.alex0d.investapp.utils.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.asPromise
import kotlinx.coroutines.async
import ru.alex0d.investapp.utils.sendEventError
import ru.alex0d.investapp.utils.sendEventResponse
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun <T : Any> CoroutineScope.promiseWithEvent(
    enable: Boolean,
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Any {
    return if (enable) {
        val caller = Uuid.random().toString()
        async(context, start, block).asPromise().then({
            sendEventResponse(caller = caller, response = it)
        }, {
            sendEventError(caller = caller, error = it.message ?: "Error query")
        })
        caller
    } else {
        async(context, start, block).asPromise()
    }
}
