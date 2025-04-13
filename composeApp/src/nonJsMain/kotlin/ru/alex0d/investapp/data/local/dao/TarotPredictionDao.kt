package ru.alex0d.investapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.alex0d.investapp.data.local.models.TarotPredictionDbo

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@Dao
actual interface TarotPredictionDao {
    @Query("SELECT * FROM tarot_predictions WHERE stock_name = :stockName")
    actual suspend fun getByStockName(stockName: String): TarotPredictionDbo?

    @Insert
    actual suspend fun insert(tarotPredictionDbo: TarotPredictionDbo)

    @Delete
    actual suspend fun delete(tarotPredictionDbo: TarotPredictionDbo)

    @Query("DELETE FROM tarot_predictions WHERE stock_name = :stockName")
    actual suspend fun delete(stockName: String)

    @Query("DELETE FROM tarot_predictions")
    actual suspend fun deleteAll()
}