package ru.alex0d.investapp.ui.composables

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import ru.alex0d.investapp.utils.toCurrencyFormat
import ru.alex0d.investapp.utils.toDecimalFormat
import kotlin.math.absoluteValue

@Composable
fun ProfitText(profit: Double, profitPercent: Double, style: TextStyle = LocalTextStyle.current) {
    val formattedProfit = profit.toCurrencyFormat("RUB")
    val formattedProfitPercent = profitPercent.absoluteValue.toDecimalFormat()

    return when {
        profit > 0 -> {
            Text(
                text = "+$formattedProfit ($formattedProfitPercent%)",
                color = Color.Green,
                style = style
            )
        }
        profit < 0 -> {
            Text(
                text = "$formattedProfit ($formattedProfitPercent%)",
                color = Color.Red,
                style = style
            )
        }
        else -> {
            Text(
                text = "$formattedProfit ($formattedProfitPercent%)",
                style = style
            )
        }
    }
}
