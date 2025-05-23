@file:OptIn(ExperimentalAdaptiveApi::class)

package ru.alex0d.investapp.screens.order

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabOptions
import investapp.composeapp.generated.resources.Res
import investapp.composeapp.generated.resources.available
import investapp.composeapp.generated.resources.buy
import investapp.composeapp.generated.resources.buying
import investapp.composeapp.generated.resources.error_occurred
import investapp.composeapp.generated.resources.go_back
import investapp.composeapp.generated.resources.lot_in_plural
import investapp.composeapp.generated.resources.lots_amount
import investapp.composeapp.generated.resources.minus
import investapp.composeapp.generated.resources.pcs_in_lot
import investapp.composeapp.generated.resources.plus
import investapp.composeapp.generated.resources.sell
import investapp.composeapp.generated.resources.selling
import investapp.composeapp.generated.resources.total_value
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveCircularProgressIndicator
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveIconButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveScaffold
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTopAppBar
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.alex0d.investapp.domain.models.Share
import ru.alex0d.investapp.screens.main.MainTabs
import ru.alex0d.investapp.screens.main.voyagerTabOptions
import ru.alex0d.investapp.ui.composables.ShareItem
import ru.alex0d.investapp.ui.composables.TabX
import ru.alex0d.investapp.ui.composables.navigationResult
import ru.alex0d.investapp.ui.theme.isSystemInDarkAdaptiveTheme
import ru.alex0d.investapp.utils.extensions.toCurrencyFormat
import ru.alex0d.investapp.utils.extensions.toIntOrZero

data class OrderScreen(
    val stockUid: String = "",
    val orderAction: OrderAction,
) : TabX {
    override val key = uniqueScreenKey
    override val options: TabOptions
        @Composable
        get() = MainTabs.ORDER.voyagerTabOptions

    @Composable
    override fun Content(
        innerPadding: PaddingValues,
    ) {
        val navigator = LocalNavigator.currentOrThrow
        val resultNavigator = navigator.navigationResult
        val coroutineScope = rememberCoroutineScope()
        val viewModel = koinViewModel<OrderViewModel>(key = this.key) {
            parametersOf(orderAction, stockUid)
        }

        val state = viewModel.state.collectAsState().value
        val availableLots = viewModel.availableLots.collectAsState().value
        val lotsInput = viewModel.lotsInput.collectAsState().value
        val totalValue = viewModel.totalValue.collectAsState().value

        LaunchedEffect(Unit) {
            viewModel.clearModel()
            viewModel.fetchOrderDetails()
        }

        AdaptiveScaffold(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            topBar = {
                OrderTopAppBar(
                    onNavigationIconClick = { navigator.pop() },
                    orderAction = orderAction
                )
            }
        ) { scaffoldPadding ->
            Box(
                modifier = Modifier
                    .padding(top = scaffoldPadding.calculateTopPadding())
                    .padding(horizontal = 2.dp)
            ) {
                when (state) {
                    is OrderDetailsState.OrderDetailsFetched -> {
                        val share = state.share
                        OrderOnDetailsFetched(
                            share = share,
                            availableLots = availableLots,
                            lotsInput = lotsInput,
                            totalValue = totalValue,
                            orderAction = orderAction,
                            onUpdateInputLots = viewModel::updateLotsInput,
                            onDecreaseLots = viewModel::decreaseLots,
                            onIncreaseLots = viewModel::increaseLots,
                            onConfirmOrder = {
                                coroutineScope.launch {
                                    viewModel.confirmOrder()
                                    resultNavigator.popWithResult(true)
                                }
                            },
                        )
                    }
                    is OrderDetailsState.Loading -> OrderOnLoading()
                    is OrderDetailsState.Error -> OrderOnError()
                }
            }
        }
    }

    @Composable
    internal fun OrderOnDetailsFetched(
        share: Share,
        availableLots: Int,
        lotsInput: String,
        totalValue: String,
        orderAction: OrderAction,
        onUpdateInputLots: (String) -> Unit,
        onDecreaseLots: () -> Unit,
        onIncreaseLots: () -> Unit,
        onConfirmOrder: () -> Unit,
    ) {
        var inTransaction by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ShareItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                share = share
            )
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = share.lastPrice.toCurrencyFormat("RUB"),
                readOnly = true,
                onValueChange = {},
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                ),
            )
            Spacer(modifier = Modifier.height(28.dp))
            LotsSection(
                lotsInput = lotsInput,
                onUpdateInputLots = onUpdateInputLots,
                onDecreaseLots = onDecreaseLots,
                onIncreaseLots = onIncreaseLots,
                orderAction = orderAction,
                availableLots = availableLots
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                if (orderAction == OrderAction.SELL) {
                    val annotatedString = buildAnnotatedString {
                        append(stringResource(Res.string.available) + ": ")
                        withLink(
                            LinkAnnotation.Clickable("available_lots") {
                                onUpdateInputLots(availableLots.toString())
                            }
                        ) {
                            append(availableLots.toString())
                        }
                        append(" " + stringResource(Res.string.lot_in_plural))
                    }
                    Text(text = annotatedString, color = Color.Gray)
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(stringResource(Res.string.pcs_in_lot, share.lot), color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (lotsInput.toIntOrZero() > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(Res.string.total_value), color = Color.Gray)
                    Text(totalValue, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            AdaptiveButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !inTransaction && lotsInput.toIntOrZero() > 0,
                onClick = {
                    inTransaction = true
                    onConfirmOrder()
                },
            ) {
                if (!inTransaction) {
                    Text(
                        text = if (orderAction == OrderAction.BUY) {
                            stringResource(Res.string.buy)
                        } else {
                            stringResource(Res.string.sell)
                        },
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 18.sp
                        )
                    )
                } else {
                    AdaptiveCircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun LotsSection(
        lotsInput: String,
        onUpdateInputLots: (String) -> Unit,
        onDecreaseLots: () -> Unit,
        onIncreaseLots: () -> Unit,
        orderAction: OrderAction,
        availableLots: Int
    ) {
        val lotsInt = lotsInput.toIntOrZero()

        Text(stringResource(Res.string.lots_amount), color = Color.Gray)
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = lotsInput,
            onValueChange = { onUpdateInputLots(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.End
                ) {
                    AdaptiveIconButton(
                        onClick = onDecreaseLots,
                        enabled = lotsInt > 0
                    ) {
                        Icon(
                            painterResource(Res.drawable.minus),
                            modifier = Modifier.size(24.dp),
                            contentDescription = "-",
                            tint = if (lotsInt > 0) {
                                if (isSystemInDarkAdaptiveTheme()) Color.White else Color.DarkGray
                            } else {
                                if (isSystemInDarkAdaptiveTheme()) Color.Gray else Color.LightGray
                            }
                        )
                    }
                    AdaptiveIconButton(
                        onClick = onIncreaseLots,
                        enabled = orderAction == OrderAction.BUY || lotsInt < availableLots
                    ) {
                        Icon(
                            painterResource(Res.drawable.plus),
                            modifier = Modifier.size(24.dp),
                            contentDescription = "+",
                            tint = if (orderAction == OrderAction.BUY || lotsInt < availableLots) {
                                if (isSystemInDarkAdaptiveTheme()) Color.White else Color.DarkGray
                            } else {
                                if (isSystemInDarkAdaptiveTheme()) Color.Gray else Color.LightGray
                            }
                        )
                    }
                }
            },
        )
    }

    @Composable
    private fun OrderOnLoading() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            AdaptiveCircularProgressIndicator()
        }
    }

    @Composable
    private fun OrderOnError() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(Res.string.error_occurred),
                textAlign = TextAlign.Center,
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun OrderTopAppBar(
        onNavigationIconClick: () -> Unit,
        orderAction: OrderAction
    ) {
        AdaptiveTopAppBar(
            navigationIcon = {
                AdaptiveIconButton(onClick = onNavigationIconClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = stringResource(Res.string.go_back)
                    )
                }
            },
            title = {
                if (orderAction == OrderAction.BUY)
                    Text(
                        text = stringResource(Res.string.buying),
                        style = MaterialTheme.typography.titleLarge
                    )
                else
                    Text(
                        text = stringResource(Res.string.selling),
                        style = MaterialTheme.typography.titleLarge
                    )
            },
            adaptation = {
                material {
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun OrderOnDetailsFetchedPreview() {
    OrderScreen(
        stockUid = "",
        orderAction = OrderAction.BUY
    ).OrderOnDetailsFetched(
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
        availableLots = 10,
        lotsInput = "5",
        totalValue = 14797.0.toCurrencyFormat("RUB"),
        orderAction = OrderAction.SELL,
        onUpdateInputLots = {},
        onDecreaseLots = {},
        onIncreaseLots = {},
        onConfirmOrder = {}
    )
}