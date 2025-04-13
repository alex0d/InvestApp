@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package ru.alex0d.investapp.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val Dispatchers.IO: CoroutineDispatcher
    get() = Dispatchers.IO