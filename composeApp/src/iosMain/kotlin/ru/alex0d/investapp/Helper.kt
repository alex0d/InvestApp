package ru.alex0d.investapp

import ru.alex0d.investapp.di.nativeModule
import ru.alex0d.investapp.di.nonJsModule
import ru.alex0d.investapp.di.startDI

fun doInitKoin() {
    startDI(nativeModule, nonJsModule)
}