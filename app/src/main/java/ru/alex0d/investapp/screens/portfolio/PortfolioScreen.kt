package ru.alex0d.investapp.screens.portfolio

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.alex0d.investapp.data.remote.models.PortfolioStockInfoDto

@Composable
fun PortfolioScreen(
    modifier: Modifier = Modifier,
    viewModel: PortfolioViewModel = koinViewModel()
) {
    val portfolioState by viewModel.portfolioState.collectAsState()
    Log.d("PortfolioScreen", "portfolioState: $portfolioState")
    LazyColumn(modifier = modifier) {
        item { OverviewCards(
            totalValue = portfolioState.totalValue,
            totalProfit = portfolioState.totalProfit,
            totalProfitPercent = portfolioState.totalProfitPercent
        ) }
        items(portfolioState.stocks) { stock ->
            StockItem(stock)
        }
    }
}

@Composable
fun OverviewCards(totalValue: Double, totalProfit: Double, totalProfitPercent: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF00504c),
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Total Balance", style = MaterialTheme.typography.titleLarge)
            Text("$totalValue $", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(4.dp))
            Text("$totalProfit $ ($totalProfitPercent%)", color = Color.Green)
        }
    }
}

@Composable
fun StockItem(stock: PortfolioStockInfoDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(stock.name, style = MaterialTheme.typography.titleMedium)
                Text("${stock.amount} shares", style = MaterialTheme.typography.bodyLarge)
            }
            Text("${stock.price} $", style = MaterialTheme.typography.titleLarge)
        }
    }
}
