package ru.alex0d.investapp.data.local

import ru.alex0d.investapp.data.local.dao.TarotPredictionDao
import ru.alex0d.investapp.data.local.dao.TarotPredictionDaoImpl

class AppDatabaseImpl : AppDatabase() {
    override fun tarotPredictionDao(): TarotPredictionDao = TarotPredictionDaoImpl()
}