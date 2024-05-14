package ru.alex0d.investapp.screens.stock

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.TarotScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import ru.alex0d.investapp.R
import ru.alex0d.investapp.domain.models.CandleInterval
import ru.alex0d.investapp.domain.models.PortfolioStockInfo
import ru.alex0d.investapp.domain.models.toStringRes
import ru.alex0d.investapp.screens.stock.candles.CandlestickChart
import ru.alex0d.investapp.ui.composables.ProfitText
import ru.alex0d.investapp.utils.MainGraph
import ru.alex0d.investapp.utils.fromHex
import ru.alex0d.investapp.utils.toCurrencyFormat

@OptIn(ExperimentalMaterial3Api::class)
@Destination<MainGraph>
@Composable
fun StockDetailsScreen(
    navigator: DestinationsNavigator,
    stockUid: String = ""
) {
    val viewModel: StockDetailsViewModel = getViewModel { parametersOf(stockUid) }
    val state = viewModel.state.collectAsState().value
    val modelProducer = viewModel.modelProducer

    Scaffold(
        topBar = {
            if (state is StockDetailsState.Success) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navigator.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                tint = Color.fromHex(state.share.textColor),
                                contentDescription = stringResource(R.string.go_back)
                            )
                        }
                    },
                    title = {
                        Column {
                            Text(
                                text = state.share.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Text(
                                text = state.share.ticker,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.LightGray
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.fromHex(state.share.backgroundColor),
                        titleContentColor = Color.fromHex(state.share.textColor)
                    ),
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .padding(horizontal = 2.dp)) {
            when (state) {
                is StockDetailsState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                is StockDetailsState.Error -> Text(state.message)
                is StockDetailsState.Success -> {
                    StockDetailsOnSuccess(
                        state = state,
                        modelProducer = modelProducer,
                        onIntervalClick = viewModel::fetchCandles,
                        navigator = navigator
                    )
                }
            }
        }
    }
}

@Composable
private fun StockDetailsOnSuccess(
    state: StockDetailsState.Success,
    modelProducer: CartesianChartModelProducer,
    onIntervalClick: (CandleInterval) -> Unit = {},
    navigator: DestinationsNavigator
) {
    Column {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            text = state.share.lastPrice.toCurrencyFormat("RUB"),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Box(
            Modifier
                .height(200.dp)
                .fillMaxWidth()
        ) {
            CandlestickChart(modelProducer)
        }
        TabsSection(onIntervalClick = onIntervalClick)
        state.stockInfo?.let { stockInfo ->
            PortfolioSection(stockInfo)
        }
        TarotSection(navigator, state)
        Spacer(modifier = Modifier.weight(1f))
        SellBuyButton(state)
    }
}

@Preview
@Composable
private fun TabsSection(
    onIntervalClick: (CandleInterval) -> Unit = {}
) {
    var selected by remember { mutableStateOf(CandleInterval.INTERVAL_DAY) }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val tabs = listOf(
            CandleInterval.INTERVAL_5_MIN,
            CandleInterval.INTERVAL_10_MIN,
            CandleInterval.INTERVAL_30_MIN,
            CandleInterval.INTERVAL_HOUR,
            CandleInterval.INTERVAL_DAY,
            CandleInterval.INTERVAL_WEEK,
            CandleInterval.INTERVAL_MONTH,
        )
        items(tabs) { tab ->
            TextButton(
                onClick = {
                    selected = tab
                    onIntervalClick(tab)
                },
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .height(30.dp)
                    .widthIn(min = 40.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = if (tab == selected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurface,
                    containerColor = if (tab == selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surface
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = stringResource(tab.toStringRes())
                )
            }
        }
    }
}

@Composable
private fun PortfolioSection(stockInfo: PortfolioStockInfo) {
    Text(
        text = stringResource(R.string.in_portfolio_text),
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Text(
                text = "${stockInfo.amount} ${stringResource(R.string.pieces_short)}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${stockInfo.averagePrice.toCurrencyFormat("RUB")} → ${
                    stockInfo.lastPrice.toCurrencyFormat(
                        "RUB"
                    )
                }",
                style = MaterialTheme.typography.bodyMedium
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )

            Text(
                text = stockInfo.totalValue.toCurrencyFormat("RUB"),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            ProfitText(
                profit = stockInfo.profit,
                profitPercent = stockInfo.profitPercent,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun TarotSection(
    navigator: DestinationsNavigator,
    state: StockDetailsState.Success
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = {
                navigator.navigate(TarotScreenDestination(stockName = state.share.name))
            },
            modifier = Modifier
                .fillMaxWidth(0.5f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Text(
                text = stringResource(R.string.esoteric_analysis),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun SellBuyButton(state: StockDetailsState.Success) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        if (state.stockInfo != null) {
            FloatingActionButton(
                modifier = Modifier.width(110.dp),
                containerColor = Color.Red,
                onClick = { /* Implement sell logic */ }
            ) {
                Text(
                    text = stringResource(R.string.sell),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 18.sp
                    ),
                    color = Color.White
                )
            }
        }
        FloatingActionButton(
            modifier = Modifier.width(110.dp),
            containerColor = Color.Green,
            onClick = { /* Implement buy logic */ }
        ) {
            Text(
                text = stringResource(R.string.buy),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 18.sp
                ),
                color = Color.Black
            )
        }
    }
}