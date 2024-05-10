package ru.alex0d.investapp.screens.portfolio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.StockDetailsScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import ru.alex0d.investapp.R
import ru.alex0d.investapp.domain.models.PortfolioStockInfo
import ru.alex0d.investapp.screens.previewproviders.FakePortfolioStockInfo
import ru.alex0d.investapp.ui.composables.ProfitText
import ru.alex0d.investapp.utils.MainGraph
import ru.alex0d.investapp.utils.toCurrencyFormat

@Destination<MainGraph>(start = true)
@Composable
fun PortfolioScreen(
    navigator: DestinationsNavigator,
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

    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface).statusBarsPadding(),
    ) {
        TotalBalanceCard(
            portfolioState.totalValue,
            portfolioState.totalProfit,
            portfolioState.totalProfitPercent
        )
        LazyColumn {
            items(portfolioState.stocks) { stock ->
                StockItem(stock, onClick = {
                    navigator.navigate(StockDetailsScreenDestination(stockUid = stock.uid))
                })
            }
        }
    }
}

@Preview
@Composable
private fun TotalBalanceCard(
    totalValue: Double = 0.0,
    totalProfit: Double = 0.0,
    totalProfitPercent: Double = 0.0
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.portfolio),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = totalValue.toCurrencyFormat("RUB"),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(4.dp))
            ProfitText(
                profit = totalProfit,
                profitPercent = totalProfitPercent
            )
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Preview
@Composable
private fun StockItem(
    @PreviewParameter(FakePortfolioStockInfo::class) stock: PortfolioStockInfo,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                model = "https://invest-brands.cdn-tinkoff.ru/${stock.logoUrl}x160.png",
                contentDescription = "${stock.name} logo"
            )
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stock.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = stock.totalValue.toCurrencyFormat("RUB"),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${stock.amount} ${stringResource(R.string.pieces_short)} · ${stock.lastPrice.toCurrencyFormat("RUB")}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    ProfitText(
                        profit = stock.profit,
                        profitPercent = stock.profitPercent,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

