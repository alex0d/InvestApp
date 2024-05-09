package ru.alex0d.investapp.utils

import java.text.NumberFormat
import java.util.Currency

internal fun Double.toCurrencyFormat(currencyCode: String): String {
    val format = NumberFormat.getCurrencyInstance().apply {
        maximumFractionDigits = 2
        currency = Currency.getInstance(currencyCode)
    }
    return format.format(this)
}

internal fun Double.toDecimalFormat(): String {
    val format = NumberFormat.getInstance().apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return format.format(this)
}