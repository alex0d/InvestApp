package ru.alex0d.investapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform