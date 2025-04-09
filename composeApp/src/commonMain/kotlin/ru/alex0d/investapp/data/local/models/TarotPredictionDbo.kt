package ru.alex0d.investapp.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tarot_predictions")
data class TarotPredictionDbo(
    @PrimaryKey @ColumnInfo(name = "stock_name") val stockName: String,
    @ColumnInfo(name = "card_name") val cardName: String,
    @ColumnInfo(name = "prediction") val prediction: String
)