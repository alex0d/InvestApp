package ru.alex0d.investapp.screens.portfolio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import ru.alex0d.investapp.data.remote.models.PortfolioStockInfoDto
import ru.alex0d.investapp.screens.previewproviders.FakePortfolioStockInfoDto

@Composable
fun PortfolioScreen(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    viewModel: PortfolioViewModel = koinViewModel()
) {
    val portfolioState by viewModel.portfolioState.collectAsState()

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> viewModel.startUpdating()
                Lifecycle.Event.ON_STOP -> viewModel.stopUpdating()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(modifier = modifier) {
        OverviewCards(
            portfolioState.totalValue,
            portfolioState.totalProfit,
            portfolioState.totalProfitPercent
        )
        LazyColumn {
            items(portfolioState.stocks) { stock ->
                StockItem(stock)
            }
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
            Text("Портфель", style = MaterialTheme.typography.titleLarge)
            Text("$totalValue ₽", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(4.dp))
            Text(
                "$totalProfit ₽ ($totalProfitPercent%)", color = when {
                    totalProfit > 0 -> Color.Green
                    totalProfit < 0 -> Color.Red
                    else -> Color.Unspecified
                }
            )
        }
    }
}

@Preview
@Composable
fun StockItem(@PreviewParameter(FakePortfolioStockInfoDto::class) stock: PortfolioStockInfoDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                model = "https://invest-brands.cdn-tinkoff.ru/${stock.logoUrl}x160.png",
                contentDescription = "${stock.name} logo"
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stock.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        "${stock.totalValue} ₽",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${stock.amount} шт · ${stock.price} ₽",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "${stock.profit} ₽ (${stock.profitPercent}%)",
                        color = when {
                            stock.profit > 0 -> Color.Green
                            stock.profit < 0 -> Color.Red
                            else -> Color.Unspecified
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
