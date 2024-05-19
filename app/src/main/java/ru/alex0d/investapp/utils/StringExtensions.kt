package ru.alex0d.investapp.utils

fun String.toIntOrZero(): Int {
    return this.toIntOrNull() ?: 0
}