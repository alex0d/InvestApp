package ru.alex0d.investapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.alex0d.investapp.data.local.dao.TarotPredictionDao
import ru.alex0d.investapp.data.local.models.TarotPredictionDbo

@Database(
    entities = [TarotPredictionDbo::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tarotPredictionDao(): TarotPredictionDao
}