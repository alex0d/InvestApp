package ru.alex0d.investapp.screens.order

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import ru.alex0d.investapp.R
import ru.alex0d.investapp.domain.models.Share
import ru.alex0d.investapp.ui.composables.ShareItem
import ru.alex0d.investapp.utils.MainGraph
import ru.alex0d.investapp.utils.extensions.toCurrencyFormat
import ru.alex0d.investapp.utils.extensions.toIntOrZero

@Destination<MainGraph>
@Composable
fun OrderScreen(
    navigator: ResultBackNavigator<Boolean>,
    orderAction: OrderAction,
    stockUid: String = ""
) {
    val coroutineScope = rememberCoroutineScope()
    val viewModel: OrderViewModel = getViewModel { parametersOf(orderAction, stockUid) }

    val state = viewModel.state.collectAsState().value
    val availableLots = viewModel.availableLots.collectAsState().value
    val lotsInput = viewModel.lotsInput.collectAsState().value
    val totalValue = viewModel.totalValue.collectAsState().value

    Scaffold(
        topBar = {
            OrderTopAppBar(
                onNavigationIconClick = { navigator.navigateBack() },
                orderAction = orderAction
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
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
                                navigator.navigateBack(true)
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
                Text(stringResource(R.string.available_lots, availableLots), color = Color.Gray)
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(stringResource(R.string.pcs_in_lot, share.lot), color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (lotsInput.toIntOrZero() > 0) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.total_value), color = Color.Gray)
                Text(totalValue, color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
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
                    text = if (orderAction == OrderAction.BUY) stringResource(R.string.buy) else stringResource(
                        R.string.sell
                    ),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 18.sp
                    )
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
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

    Text(stringResource(R.string.lots_amount), color = Color.Gray)
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
                IconButton(
                    onClick = onDecreaseLots,
                    enabled = lotsInt > 0
                ) {
                    Icon(
                        painterResource(id = R.drawable.minus),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "-",
                        tint = if (lotsInt > 0) {
                            if (isSystemInDarkTheme()) Color.White else Color.DarkGray
                        } else {
                            if (isSystemInDarkTheme()) Color.Gray else Color.LightGray
                        }
                    )
                }
                IconButton(
                    onClick = onIncreaseLots,
                    enabled = orderAction == OrderAction.BUY || lotsInt < availableLots
                ) {
                    Icon(
                        painterResource(id = R.drawable.plus),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "+",
                        tint = if (orderAction == OrderAction.BUY || lotsInt < availableLots) {
                            if (isSystemInDarkTheme()) Color.White else Color.DarkGray
                        } else {
                            if (isSystemInDarkTheme()) Color.Gray else Color.LightGray
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
        CircularProgressIndicator()
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
            text = stringResource(R.string.error_occurred),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun OrderTopAppBar(
    onNavigationIconClick: () -> Unit,
    orderAction: OrderAction
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = stringResource(R.string.go_back)
                )
            }
        },
        title = {
            if (orderAction == OrderAction.BUY)
                Text(
                    text = stringResource(R.string.buying),
                    style = MaterialTheme.typography.titleLarge
                )
            else
                Text(
                    text = stringResource(R.string.selling),
                    style = MaterialTheme.typography.titleLarge
                )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun OrderOnDetailsFetchedPreview() {
    OrderOnDetailsFetched(
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