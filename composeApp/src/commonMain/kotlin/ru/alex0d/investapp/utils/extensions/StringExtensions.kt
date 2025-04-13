package ru.alex0d.investapp.utils.extensions

fun String.toIntOrZero(): Int {
    return this.toIntOrNull() ?: 0
}