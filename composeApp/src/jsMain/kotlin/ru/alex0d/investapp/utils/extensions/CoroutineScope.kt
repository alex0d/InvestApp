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

fun <T : Any> CoroutineScope.promise(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T?
): Any {
    return async(context, start, block).asPromise()
}

@OptIn(ExperimentalUuidApi::class)
fun <T : Any> CoroutineScope.promiseWithEvent(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T?
): Any {
    val caller = Uuid.random().toString()
    async(context, start, block).asPromise().then({
        sendEventResponse(caller = caller, response = it)
    }, {
        sendEventError(caller = caller, error = it.message ?: "Error query")
    })
    return caller
}
