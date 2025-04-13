package ru.alex0d.investapp.data.local

import ru.alex0d.investapp.data.local.dao.TarotPredictionDao

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect abstract class AppDatabase {
    abstract fun tarotPredictionDao(): TarotPredictionDao
}