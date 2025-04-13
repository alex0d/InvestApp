package ru.alex0d.investapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import ru.alex0d.investapp.di.nativeModule
import ru.alex0d.investapp.di.startDI
import ru.alex0d.investapp.di.wasmJsModule

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startDI(nativeModule, wasmJsModule)

    ComposeViewport(document.body!!) {
        App()
    }
}