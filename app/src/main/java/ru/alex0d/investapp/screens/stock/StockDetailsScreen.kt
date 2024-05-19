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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.OrderScreenDestination
import com.ramcosta.composedestinations.generated.destinations.TarotScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import ru.alex0d.investapp.R
import ru.alex0d.investapp.domain.models.CandleInterval
import ru.alex0d.investapp.domain.models.PortfolioStockInfo
import ru.alex0d.investapp.domain.models.Share
import ru.alex0d.investapp.domain.models.toStringRes
import ru.alex0d.investapp.screens.order.OrderAction
import ru.alex0d.investapp.screens.stock.chart.StockChart
import ru.alex0d.investapp.ui.composables.ProfitText
import ru.alex0d.investapp.utils.MainGraph
import ru.alex0d.investapp.utils.fromHex
import ru.alex0d.investapp.utils.toCurrencyFormat

@Destination<MainGraph>
@Composable
fun StockDetailsScreen(
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<OrderScreenDestination, Boolean>,
    stockUid: String = ""
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: StockDetailsViewModel = getViewModel { parametersOf(stockUid) }
    val state = viewModel.state.collectAsState().value
    val modelProducer = viewModel.modelProducer

    val orderMessage = stringResource(R.string.order_completed)
    resultRecipient.onNavResult { result ->
        if (result is NavResult.Value && result.value) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = orderMessage,
                    withDismissAction = true
                )
                viewModel.fetchStockDetails()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) {
            Snackbar(
                snackbarData = it,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                dismissActionContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        } },
        topBar = {
            if (state is StockDetailsState.Success) {
                StockDetailsTopAppBar(navigator, state)
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(top = padding.calculateTopPadding())
            .padding(horizontal = 2.dp)) {
            when (state) {
                is StockDetailsState.Success -> {
                    StockDetailsOnSuccess(
                        state = state,
                        modelProducer = modelProducer,
                        onSwitchChartType = viewModel::chartType::set,
                        onIntervalClick = viewModel::fetchCandles,
                        navigator = navigator
                    )
                }
                is StockDetailsState.Loading -> OrderDetailsOnLoading()
                is StockDetailsState.Error -> OrderDetailsOnError()
            }
        }
    }
}

@Composable
private fun StockDetailsOnSuccess(
    state: StockDetailsState.Success,
    modelProducer: CartesianChartModelProducer,
    onSwitchChartType: (ChartType) -> Unit = {},
    onIntervalClick: (CandleInterval) -> Unit = {},
    navigator: DestinationsNavigator
) {
    var chartType by remember { mutableStateOf(ChartType.LINE) }

    Column {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 10.dp, bottom = 0.dp)
                .height(36.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        ) {
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = state.share.lastPrice.toCurrencyFormat("RUB"),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    chartType =
                        if (chartType == ChartType.LINE) ChartType.CANDLES else ChartType.LINE
                    onSwitchChartType(chartType)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Icon(
                    painter = painterResource(id = if (chartType == ChartType.LINE) R.drawable.line_chart else R.drawable.candlestick_chart),
                    contentDescription = if (chartType == ChartType.LINE) stringResource(R.string.switch_to_candlestick_chart) else stringResource(R.string.switch_to_line_chart),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
        Box(
            Modifier
                .height(200.dp)
                .fillMaxWidth()
        ) {
            StockChart(modelProducer)
        }
        IntervalTabsSection(onIntervalClick = onIntervalClick)
        state.stockInfo?.let { stockInfo ->
            PortfolioSection(stockInfo)
        } ?: run {
            TarotSection(navigator, state)
        }
        Spacer(modifier = Modifier.weight(1f))
        ButtonsSection(navigator, state)
    }
}

@Composable
private fun IntervalTabsSection(
    onIntervalClick: (CandleInterval) -> Unit
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
                    containerColor = if (tab == selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                    contentColor = if (tab == selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
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
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surfaceContainerHighest)
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
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text(
                text = stringResource(R.string.esoteric_analysis),
            )
        }
    }
}

@Composable
private fun ButtonsSection(
    navigator: DestinationsNavigator,
    state: StockDetailsState.Success
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (state.stockInfo != null) {
            IconButton(
                modifier = Modifier.width(60.dp),
                onClick = { navigator.navigate(TarotScreenDestination(stockName = state.share.name)) },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer

                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cards),
                    contentDescription = stringResource(R.string.esoteric_analysis),
                )
            }
            Button(
                modifier = Modifier.width(120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    contentColor = MaterialTheme.colorScheme.surface
                ),
                onClick = {
                    navigator.navigate(OrderScreenDestination(orderAction = OrderAction.SELL, stockUid = state.share.uid))
                },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(
                    text = stringResource(R.string.sell),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 16.sp
                    ),
                )
            }
        }
        Button(
            modifier = Modifier.width(120.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4376F8),
                contentColor = Color.White
            ),
            onClick = {
                navigator.navigate(OrderScreenDestination(orderAction = OrderAction.BUY, stockUid = state.share.uid))
            }
        ) {
            Text(
                text = stringResource(R.string.buy),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 16.sp
                ),
            )
        }
    }
}

@Composable
private fun OrderDetailsOnLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun OrderDetailsOnError() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.error_occurred),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun StockDetailsTopAppBar(
    navigator: DestinationsNavigator,
    state: StockDetailsState.Success
) {
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

@Preview(showBackground = true)
@Composable
private fun StockDetailsOnSuccessNotInPortfolioPreview() {
    StockDetailsOnSuccess(
        state = StockDetailsState.Success(
            share = Share(
                uid = "64c0da45-4c90-41d4-b053-0c66c7a8ddcd",
                figi = "TCS109029557",
                ticker = "SBERP",
                classCode = "SPEQ",
                isin = "RU0009029557",
                currency = "rub",
                name = "Сбер Банк - привилегированные акции",
                countryOfRisk = "RU",
                countryOfRiskName = "Российская Федерация",
                sector = "financial",
                lot = 1,
                lastPrice = 295.94,
                url = "sber3",
                backgroundColor = "#309c0b",
                textColor = "#ffffff"
            ),
            stockInfo = null,
        ),
        modelProducer = CartesianChartModelProducer.build(),
        onSwitchChartType = {},
        onIntervalClick = {},
        navigator = EmptyDestinationsNavigator
    )
}

@Preview(showBackground = true)
@Composable
private fun StockDetailsOnSuccessInPortfolioPreview() {
    StockDetailsOnSuccess(
        state = StockDetailsState.Success(
            share = Share(
                uid = "64c0da45-4c90-41d4-b053-0c66c7a8ddcd",
                figi = "TCS109029557",
                ticker = "SBERP",
                classCode = "SPEQ",
                isin = "RU0009029557",
                currency = "rub",
                name = "Сбер Банк - привилегированные акции",
                countryOfRisk = "RU",
                countryOfRiskName = "Российская Федерация",
                sector = "financial",
                lot = 1,
                lastPrice = 295.94,
                url = "sber3",
                backgroundColor = "#309c0b",
                textColor = "#ffffff"
            ),
            stockInfo = PortfolioStockInfo(
                uid = "64c0da45-4c90-41d4-b053-0c66c7a8ddcd",
                ticker = "SBERP",
                name = "Сбер Банк - привилегированные акции",
                amount = 10,
                averagePrice = 350.15,
                lastPrice = 308.15,
                totalValue = 3081.50,
                profit = -215.10,
                profitPercent = -10.00,
                logoUrl = "sber3",
                backgroundColor = "#309c0b",
                textColor = "#ffffff"
            ),
        ),
        modelProducer = CartesianChartModelProducer.build(),
        onSwitchChartType = {},
        onIntervalClick = {},
        navigator = EmptyDestinationsNavigator
    )
}
