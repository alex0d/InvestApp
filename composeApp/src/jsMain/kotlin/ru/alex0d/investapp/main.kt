package ru.alex0d.investapp

import ru.alex0d.investapp.di.jsModule
import ru.alex0d.investapp.di.nativeModule
import ru.alex0d.investapp.di.startDI

fun main() {
    startDI(nativeModule, jsModule)
}