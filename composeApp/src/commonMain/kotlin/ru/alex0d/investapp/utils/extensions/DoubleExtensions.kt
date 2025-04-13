package ru.alex0d.investapp.utils.extensions

internal expect fun Double.toCurrencyFormat(currencyCode: String): String

internal expect fun Double.toDecimalFormat(): String