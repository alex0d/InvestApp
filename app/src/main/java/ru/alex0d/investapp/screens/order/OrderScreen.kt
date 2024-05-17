package ru.alex0d.investapp.screens.order

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import ru.alex0d.investapp.R
import ru.alex0d.investapp.ui.composables.ShareItem
import ru.alex0d.investapp.utils.MainGraph
import ru.alex0d.investapp.utils.isDarkThemeOn
import ru.alex0d.investapp.utils.toCurrencyFormat

@OptIn(ExperimentalMaterial3Api::class)
@Destination<MainGraph>
@Composable
fun OrderScreen(
    navigator: ResultBackNavigator<Boolean>,
    orderAction: OrderAction,
    stockUid: String = ""
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val viewModel: OrderViewModel = getViewModel { parametersOf(orderAction, stockUid) }

    val state = viewModel.state.collectAsState().value
    val availableLots = viewModel.availableLots.collectAsState().value
    val inputLots = viewModel.inputLots.collectAsState().value
    val totalValue = viewModel.totalValue.collectAsState().value

    var inTransaction by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateBack() }) {
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
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 2.dp)
        ) {
            when (state) {
                is OrderDetailsState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is OrderDetailsState.OrderDetailsFetched -> {
                    val share = state.share

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

                        Text(stringResource(R.string.lots_amount), color = Color.Gray)
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = inputLots.toString(),
                            onValueChange = { newValue: String -> viewModel.updateInputLots(newValue) },
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
                                        onClick = { viewModel.decreaseLots() },
                                        enabled = inputLots > 0
                                    ) {
                                        Icon(
                                            painterResource(id = R.drawable.minus),
                                            modifier = Modifier.size(24.dp),
                                            contentDescription = null,
                                            tint = if (inputLots > 0) {
                                                if (context.isDarkThemeOn()) Color.White else Color.DarkGray
                                            } else {
                                                if (context.isDarkThemeOn()) Color.Gray else Color.LightGray
                                            }
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.increaseLots() },
                                        enabled = orderAction == OrderAction.BUY || inputLots < availableLots
                                    ) {
                                        Icon(
                                            painterResource(id = R.drawable.plus),
                                            modifier = Modifier.size(24.dp),
                                            contentDescription = null,
                                            tint = if (orderAction == OrderAction.BUY || inputLots < availableLots) {
                                                if (context.isDarkThemeOn()) Color.White else Color.DarkGray
                                            } else {
                                                if (context.isDarkThemeOn()) Color.Gray else Color.LightGray
                                            }
                                        )
                                    }
                                }
                            },
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
                        if (inputLots > 0) {
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
                            enabled = !inTransaction,
                            onClick = {
                                inTransaction = true
                                coroutineScope.launch {
                                    val result = viewModel.confirmOrder()
                                    navigator.navigateBack(result)
                                }
                            },
                        ) {
                            if (!inTransaction) {
                                Text(
                                    text = if (orderAction == OrderAction.BUY) stringResource(R.string.buy) else stringResource(R.string.sell),
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

                is OrderDetailsState.Error -> {
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
        }
    }
}