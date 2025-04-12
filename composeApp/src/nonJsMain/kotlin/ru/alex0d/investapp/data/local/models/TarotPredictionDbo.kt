@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package ru.alex0d.investapp.data.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tarot_predictions")
actual data class TarotPredictionDbo actual constructor(
    @PrimaryKey @ColumnInfo(name = "stock_name")
    actual val stockName: String,

    @ColumnInfo(name = "card_name")
    actual val cardName: String,

    @ColumnInfo(name = "prediction")
    actual val prediction: String
)