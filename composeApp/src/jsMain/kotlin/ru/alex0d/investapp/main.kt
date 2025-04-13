package ru.alex0d.investapp

import ru.alex0d.investapp.di.jsModule
import ru.alex0d.investapp.di.nativeModule
import ru.alex0d.investapp.di.startDI
import ru.alex0d.investapp.utils.sendEvent

fun main() {
    startDI(nativeModule, jsModule)
}

/**
 * Init fun for run after ready index.html
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
fun init() = sendEvent("Init")