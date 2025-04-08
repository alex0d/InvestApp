package ru.alex0d.investapp.screens.portfolio

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import ru.alex0d.investapp.R
import ru.alex0d.investapp.domain.models.PortfolioInfo
import ru.alex0d.investapp.domain.models.PortfolioStockInfo
import ru.alex0d.investapp.screens.main.MainTabs
import ru.alex0d.investapp.screens.main.voyagerTabOptions
import ru.alex0d.investapp.screens.stock.StockDetailsScreen
import ru.alex0d.investapp.ui.composables.ProfitText
import ru.alex0d.investapp.ui.composables.TabX
import ru.alex0d.investapp.utils.extensions.toCurrencyFormat
import ru.alex0d.investapp.utils.previewproviders.FakePortfolioInfo

class PortfolioScreen : TabX {
    override val options: TabOptions
        @Composable
        get() = MainTabs.PORTFOLIO.voyagerTabOptions

    @Composable
    override fun Content(
        innerPadding: PaddingValues,
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val viewModel = koinViewModel<PortfolioViewModel>()
        val state = viewModel.state.collectAsState().value

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

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            when (state) {
                is PortfolioState.Loading -> PortfolioScreenOnLoading()
                is PortfolioState.PortfolioInfoFetched -> PortfolioScreenOnInfoFetched(
                    portfolioInfo = state.portfolioInfo,
                )
                is PortfolioState.Error -> PortfolioScreenOnError()
            }
        }
    }

    @Preview
    @Composable
    private fun PortfolioScreenOnInfoFetched(
        @PreviewParameter(FakePortfolioInfo::class) portfolioInfo: PortfolioInfo,
    ) {
        val navigator = LocalNavigator.current
        Column {
            TotalBalanceCard(
                portfolioInfo.totalValue,
                portfolioInfo.totalProfit,
                portfolioInfo.totalProfitPercent
            )
            if (portfolioInfo.stocks.isNotEmpty()) {
                LazyColumn {
                    items(portfolioInfo.stocks, key = { it.uid }) { stock ->
                        StockItem(
                            stock = stock,
                            onClick = {
                                navigator?.push(StockDetailsScreen(stockUid = stock.uid))
                            },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.empty_portfolio_message),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(24.dp))
                    Icon(
                        painterResource(id = R.drawable.sentiment_dissatisfied),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.size(48.dp)
                    )
                }

            }
        }
    }

    @Composable
    private fun TotalBalanceCard(
        totalValue: Double,
        totalProfit: Double,
        totalProfitPercent: Double
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

    @Composable
    private fun StockItem(
        stock: PortfolioStockInfo,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
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
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.End
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${stock.amount} ${stringResource(R.string.pieces_short)} · ${
                                stock.lastPrice.toCurrencyFormat(
                                    "RUB"
                                )
                            }",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(Modifier.width(2.dp))
                        ProfitText(
                            profit = stock.profit,
                            profitPercent = stock.profitPercent,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun PortfolioScreenOnLoading() {
        val infiniteTransition = rememberInfiniteTransition(label = "infinite")

        val topCardColor by infiniteTransition.animateColor(
            initialValue = MaterialTheme.colorScheme.primaryContainer,
            targetValue = MaterialTheme.colorScheme.secondaryContainer,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "topCardColor"
        )

        val itemsColor by infiniteTransition.animateColor(
            initialValue = MaterialTheme.colorScheme.surfaceVariant,
            targetValue = MaterialTheme.colorScheme.secondaryContainer,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "itemsColor"
        )

        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                    .height(128.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = topCardColor,
                )
            ) { }
            Spacer(Modifier.height(8.dp))
            Column {
                repeat(5) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .height(68.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = itemsColor
                        )
                    ) { }
                }
            }
        }
    }

    @Composable
    private fun PortfolioScreenOnError() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.error_occurred),
                textAlign = TextAlign.Center,
            )
        }
    }
}
