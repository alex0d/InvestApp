@file:OptIn(ExperimentalAdaptiveApi::class)

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabOptions
import investapp.composeapp.generated.resources.Res
import investapp.composeapp.generated.resources.buy
import investapp.composeapp.generated.resources.candlestick_chart
import investapp.composeapp.generated.resources.cards
import investapp.composeapp.generated.resources.error_occurred
import investapp.composeapp.generated.resources.esoteric_analysis
import investapp.composeapp.generated.resources.go_back
import investapp.composeapp.generated.resources.in_portfolio_text
import investapp.composeapp.generated.resources.line_chart
import investapp.composeapp.generated.resources.order_completed
import investapp.composeapp.generated.resources.pieces_short
import investapp.composeapp.generated.resources.sell
import investapp.composeapp.generated.resources.switch_to_candlestick_chart
import investapp.composeapp.generated.resources.switch_to_line_chart
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveCircularProgressIndicator
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveHorizontalDivider
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveIconButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveScaffold
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.alex0d.investapp.domain.models.CandleInterval
import ru.alex0d.investapp.domain.models.PortfolioStockInfo
import ru.alex0d.investapp.domain.models.Share
import ru.alex0d.investapp.domain.models.toStringRes
import ru.alex0d.investapp.getPlatform
import ru.alex0d.investapp.screens.main.MainTabs
import ru.alex0d.investapp.screens.main.voyagerTabOptions
import ru.alex0d.investapp.screens.order.OrderAction
import ru.alex0d.investapp.screens.order.OrderScreen
import ru.alex0d.investapp.screens.stock.chart.ChartModel
import ru.alex0d.investapp.screens.stock.chart.EmptyChartModel
import ru.alex0d.investapp.screens.stock.chart.StockChart
import ru.alex0d.investapp.screens.tarot.TarotScreen
import ru.alex0d.investapp.ui.composables.ProfitText
import ru.alex0d.investapp.ui.composables.TabX
import ru.alex0d.investapp.ui.composables.navigationResult
import ru.alex0d.investapp.utils.extensions.fromHex
import ru.alex0d.investapp.utils.extensions.toCurrencyFormat

data class StockDetailsScreen(
    val stockUid: String,
) : TabX {
    override val key = uniqueScreenKey
    override val options: TabOptions
        @Composable
        get() = MainTabs.STOCK_DETAILS.voyagerTabOptions

    @Composable
    override fun Content(
        innerPadding: PaddingValues,
    ) {
        val navigator = LocalNavigator.currentOrThrow
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        val viewModel = koinViewModel<StockDetailsViewModel>(key = this.key) {
            parametersOf(stockUid)
        }
        val state = viewModel.state.collectAsState().value

        val orderMessage = stringResource(Res.string.order_completed)
        val navResult = navigator.navigationResult.getLastResult<Boolean>().value
        LaunchedEffect(navResult) {
            if (navResult == true) {
                scope.launch {
                    viewModel.fetchStockDetails()
                    snackbarHostState.showSnackbar(
                        message = orderMessage,
                        withDismissAction = true
                    )
                }
            }
        }

        AdaptiveScaffold(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) {
                    Snackbar(
                        snackbarData = it,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        dismissActionContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            },
            topBar = {
                if (state is StockDetailsState.Success) {
                    StockDetailsTopAppBar(state)
                }
            }
        ) { scaffoldPadding ->
            Box(
                modifier = Modifier
                    .padding(top = scaffoldPadding.calculateTopPadding())
                    .padding(horizontal = 2.dp)
            ) {
                when (state) {
                    is StockDetailsState.Success -> {
                        StockDetailsOnSuccess(
                            state = state,
                            chartModel = viewModel.chartModel,
                            onSwitchChartType = viewModel::chartType::set,
                            onIntervalClick = viewModel::fetchCandles,
                        )
                    }
                    is StockDetailsState.Loading -> OrderDetailsOnLoading()
                    is StockDetailsState.Error -> OrderDetailsOnError()
                }
            }
        }
    }

    @Composable
    internal fun StockDetailsOnSuccess(
        state: StockDetailsState.Success,
        chartModel: ChartModel,
        onSwitchChartType: (ChartType) -> Unit = {},
        onIntervalClick: (CandleInterval) -> Unit = {},
    ) {
        Column {  // ColumnScope is needed for applying weight modifier
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f, fill = true)
            ) {
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
                    if ("Web" !in getPlatform().name) {
                        ChartTypeSwitchButton(onSwitchChartType)
                    }
                }
                Box(
                    Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                ) {
                    StockChart(chartModel)
                }
                IntervalTabsSection(onIntervalClick = onIntervalClick)
                state.stockInfo?.let { stockInfo ->
                    PortfolioSection(stockInfo)
                } ?: run {
                    TarotSection(state)
                }
                Spacer(modifier = Modifier.weight(1f))
                ButtonsSection(state)
            }
        }
    }

    @Composable
    private fun ChartTypeSwitchButton(
        onSwitchChartType: (ChartType) -> Unit
    ) {
        var chartType by remember { mutableStateOf(ChartType.LINE) }
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
                painter = painterResource(if (chartType == ChartType.LINE) Res.drawable.line_chart else Res.drawable.candlestick_chart),
                contentDescription = if (chartType == ChartType.LINE) {
                    stringResource(Res.string.switch_to_candlestick_chart)
                } else {
                    stringResource(Res.string.switch_to_line_chart)
                },
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
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
            text = stringResource(Res.string.in_portfolio_text),
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
                    text = "${stockInfo.amount} ${stringResource(Res.string.pieces_short)}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))

                val symbol = if ("Web" in getPlatform().name) "->" else "→"
                Text(
                    text = "${stockInfo.averagePrice.toCurrencyFormat("RUB")} $symbol ${
                        stockInfo.lastPrice.toCurrencyFormat(
                            "RUB"
                        )
                    }",
                    style = MaterialTheme.typography.bodyMedium
                )

                AdaptiveHorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
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
    private fun TarotSection(state: StockDetailsState.Success) {
        val navigator = LocalNavigator.current
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            AdaptiveButton(
                onClick = {
                    navigator?.push(TarotScreen(stockName = state.share.name))
                },
                adaptation = {
                    material {
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
            ) {
                Text(
                    text = stringResource(Res.string.esoteric_analysis),
                )
            }
        }
    }

    @Composable
    private fun ButtonsSection(state: StockDetailsState.Success) {
        val navigator = LocalNavigator.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (state.stockInfo != null) {
                IconButton(
                    modifier = Modifier.width(60.dp),
                    onClick = {
                        navigator?.push(TarotScreen(stockName = state.share.name))
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer

                    )
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.cards),
                        contentDescription = stringResource(Res.string.esoteric_analysis),
                    )
                }
                Button(
                    modifier = Modifier.width(120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface,
                        contentColor = MaterialTheme.colorScheme.surface
                    ),
                    onClick = {
                        navigator?.push(
                            OrderScreen(
                                orderAction = OrderAction.SELL,
                                stockUid = state.share.uid
                            )
                        )
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = stringResource(Res.string.sell),
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
                    navigator?.push(OrderScreen(orderAction = OrderAction.BUY, stockUid = state.share.uid))
                }
            ) {
                Text(
                    text = stringResource(Res.string.buy),
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
            AdaptiveCircularProgressIndicator()
        }
    }

    @Composable
    internal fun OrderDetailsOnError() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.error_occurred),
                textAlign = TextAlign.Center,
            )
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    private fun StockDetailsTopAppBar(state: StockDetailsState.Success) {
        val navigator = LocalNavigator.current
        TopAppBar(
            navigationIcon = {
                AdaptiveIconButton(onClick = { navigator?.pop() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = Color.fromHex(state.share.textColor),
                        contentDescription = stringResource(Res.string.go_back)
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

@Preview
@Composable
private fun StockDetailsOnSuccessNotInPortfolioPreview() {
    StockDetailsScreen(stockUid = "")
        .StockDetailsOnSuccess(
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
            chartModel = EmptyChartModel,
            onSwitchChartType = {},
            onIntervalClick = {},
        )
}

@Preview
@Composable
private fun StockDetailsOnSuccessInPortfolioPreview() {
    StockDetailsScreen(stockUid = "")
        .StockDetailsOnSuccess(
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
            chartModel = EmptyChartModel,
            onSwitchChartType = {},
            onIntervalClick = {},
        )
}