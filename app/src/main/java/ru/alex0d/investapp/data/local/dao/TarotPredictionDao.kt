package ru.alex0d.investapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.alex0d.investapp.data.local.models.TarotPredictionDbo

@Dao
interface TarotPredictionDao {
    @Query("SELECT * FROM tarot_predictions WHERE stock_name = :stockName")
    suspend fun getByStockName(stockName: String): TarotPredictionDbo?

    @Insert
    suspend fun insert(tarotPredictionDbo: TarotPredictionDbo)

    @Delete
    suspend fun delete(tarotPredictionDbo: TarotPredictionDbo)

    @Query("DELETE FROM tarot_predictions")
    suspend fun deleteAll()
}