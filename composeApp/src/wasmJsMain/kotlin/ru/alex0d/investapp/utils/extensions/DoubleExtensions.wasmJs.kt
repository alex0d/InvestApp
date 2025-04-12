package ru.alex0d.investapp.utils.extensions

external fun toCurrencyFormat(value: Double, currencyCode: String): String
external fun toDecimalFormat(value: Double): String

internal actual fun Double.toCurrencyFormat(currencyCode: String) = toCurrencyFormat(this, currencyCode)

internal actual fun Double.toDecimalFormat() = toDecimalFormat(this)