package ru.alex0d.investapp.utils

import kotlinx.browser.document
import org.w3c.dom.CustomEvent
import org.w3c.dom.CustomEventInit

fun sendEvent(
    caller: String,
) = sendEvent(
    caller = caller,
    response = null,
    error = null
)

fun sendEventError(
    caller: String,
    error: String,
) = sendEvent(
    caller = caller,
    response = null,
    error = error
)

fun <T> sendEventResponse(
    caller: String,
    response: T,
) = sendEvent(
    caller = caller,
    response = response,
    error = null
)

@Suppress("UNUSED_PARAMETER")
private fun <T> sendEvent(
    caller: String,
    response: T?,
    error: String?,
) {
    document.dispatchEvent(
        CustomEvent(
            type = "framescript:log",
            eventInitDict = CustomEventInit(
                detail = js("{'caller': caller, 'response': response, 'error': error}")
            ),
        )
    )
}
