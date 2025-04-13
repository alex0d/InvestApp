@file:OptIn(ExperimentalAdaptiveApi::class)

package ru.alex0d.investapp.screens.tarot

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.TabOptions
import investapp.composeapp.generated.resources.Res
import investapp.composeapp.generated.resources.death
import investapp.composeapp.generated.resources.error_occurred
import investapp.composeapp.generated.resources.esoteric_analysis
import investapp.composeapp.generated.resources.go_back
import investapp.composeapp.generated.resources.justice
import investapp.composeapp.generated.resources.not_iir_disclaimer
import investapp.composeapp.generated.resources.refresh
import investapp.composeapp.generated.resources.strength
import investapp.composeapp.generated.resources.tarot_death
import investapp.composeapp.generated.resources.tarot_justice
import investapp.composeapp.generated.resources.tarot_strength
import investapp.composeapp.generated.resources.tarot_temperance
import investapp.composeapp.generated.resources.tarot_the_chariot
import investapp.composeapp.generated.resources.tarot_the_devil
import investapp.composeapp.generated.resources.tarot_the_emperor
import investapp.composeapp.generated.resources.tarot_the_empress
import investapp.composeapp.generated.resources.tarot_the_fool
import investapp.composeapp.generated.resources.tarot_the_hanged_man
import investapp.composeapp.generated.resources.tarot_the_hermit
import investapp.composeapp.generated.resources.tarot_the_hierophant
import investapp.composeapp.generated.resources.tarot_the_high_priestess
import investapp.composeapp.generated.resources.tarot_the_lovers
import investapp.composeapp.generated.resources.tarot_the_magician
import investapp.composeapp.generated.resources.tarot_the_moon
import investapp.composeapp.generated.resources.tarot_the_sun
import investapp.composeapp.generated.resources.tarot_the_tower
import investapp.composeapp.generated.resources.tarot_wheel_of_fortune
import investapp.composeapp.generated.resources.temperance
import investapp.composeapp.generated.resources.the_chariot
import investapp.composeapp.generated.resources.the_devil
import investapp.composeapp.generated.resources.the_emperor
import investapp.composeapp.generated.resources.the_empress
import investapp.composeapp.generated.resources.the_fool
import investapp.composeapp.generated.resources.the_hanged_man
import investapp.composeapp.generated.resources.the_hermit
import investapp.composeapp.generated.resources.the_hierophant
import investapp.composeapp.generated.resources.the_high_priestess
import investapp.composeapp.generated.resources.the_lovers
import investapp.composeapp.generated.resources.the_magician
import investapp.composeapp.generated.resources.the_moon
import investapp.composeapp.generated.resources.the_sun
import investapp.composeapp.generated.resources.the_tower
import investapp.composeapp.generated.resources.updating_prediction_text
import investapp.composeapp.generated.resources.updating_prediction_title
import investapp.composeapp.generated.resources.wheel_of_fortune
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveCircularProgressIndicator
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveIconButton
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveScaffold
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTopAppBar
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.alex0d.investapp.domain.models.TarotCard
import ru.alex0d.investapp.screens.main.MainTabs
import ru.alex0d.investapp.screens.main.voyagerTabOptions
import ru.alex0d.investapp.ui.composables.TabX
import ru.alex0d.investapp.ui.composables.TwoButtonsAlertDialog

data class TarotScreen(
    val stockName: String = "",
) : TabX {
    override val key = uniqueScreenKey
    override val options: TabOptions
        @Composable
        get() = MainTabs.TAROT.voyagerTabOptions

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content(
        innerPadding: PaddingValues,
    ) {
        val navigator = LocalNavigator.current
        val viewModel = koinViewModel<TarotViewModel>(key = this.key) { parametersOf(stockName) }
        val state = viewModel.state.collectAsState().value

        var openAlertDialog by remember { mutableStateOf(false) }

        AdaptiveScaffold(
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
            topBar = {
                if (state is TarotPredictionState.Success) {
                    AdaptiveTopAppBar(
                        navigationIcon = {
                            AdaptiveIconButton(onClick = { navigator?.pop() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    tint = MaterialTheme.colorScheme.primary,
                                    contentDescription = stringResource(Res.string.go_back)
                                )
                            }
                        },
                        title = {
                            Text(
                                text = stringResource(Res.string.esoteric_analysis),
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        actions = {
                            AdaptiveIconButton(onClick = { openAlertDialog = true }) {
                                Icon(
                                    painter = painterResource(Res.drawable.refresh),
                                    contentDescription = stringResource(Res.string.refresh),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        adaptation = {
                            material {
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                )
                            }
                        },
                    )
                }
            }
        ) { scaffoldPadding ->
            Box(
                modifier = Modifier
                    .padding(top = scaffoldPadding.calculateTopPadding())
                    .padding(horizontal = 2.dp)
            ) {
                when (state) {
                    is TarotPredictionState.Loading -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AdaptiveCircularProgressIndicator()
                    }

                    is TarotPredictionState.Error -> Box(
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

                    is TarotPredictionState.Success -> TarotPredictionOnSuccess(stockName, state)
                }

                if (openAlertDialog) {
                    TwoButtonsAlertDialog(
                        onDismissRequest = { openAlertDialog = false },
                        onConfirmation = {
                            openAlertDialog = false
                            viewModel.refreshTarotPrediction()
                        },
                        dialogTitle = stringResource(Res.string.updating_prediction_title),
                        dialogText = stringResource(Res.string.updating_prediction_text),
                        icon = Icons.Default.Warning
                    )
                }
            }
        }
    }

    @Composable
    private fun TarotPredictionOnSuccess(
        stockName: String,
        state: TarotPredictionState.Success
    ) {
        var visible by remember { mutableStateOf(false) }

        LaunchedEffect(visible) {
            if (!visible) {
                delay(150)
                visible = true
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(visible = visible) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 20.dp),
                        text = stockName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            lineBreak = LineBreak.Paragraph,
                        ),
                        lineHeight = 28.sp,
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp,
                    )
                    Image(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .clip(RoundedCornerShape(16.dp)),
                        painter = painterResource(getTarotImage(state.prediction.card)),
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = stringResource(getTarotCardName(state.prediction.card)),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 24.sp,
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = state.prediction.prediction,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.size(32.dp))
                    Text(
                        text = stringResource(Res.string.not_iir_disclaimer),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

private fun getTarotImage(card: TarotCard): DrawableResource {
    return when (card) {
        TarotCard.THE_FOOL -> Res.drawable.tarot_the_fool
        TarotCard.THE_MAGICIAN -> Res.drawable.tarot_the_magician
        TarotCard.THE_HIGH_PRIESTESS -> Res.drawable.tarot_the_high_priestess
        TarotCard.THE_EMPRESS -> Res.drawable.tarot_the_empress
        TarotCard.THE_EMPEROR -> Res.drawable.tarot_the_emperor
        TarotCard.THE_HIEROPHANT -> Res.drawable.tarot_the_hierophant
        TarotCard.THE_LOVERS -> Res.drawable.tarot_the_lovers
        TarotCard.THE_CHARIOT -> Res.drawable.tarot_the_chariot
        TarotCard.JUSTICE -> Res.drawable.tarot_justice
        TarotCard.THE_HERMIT -> Res.drawable.tarot_the_hermit
        TarotCard.WHEEL_OF_FORTUNE -> Res.drawable.tarot_wheel_of_fortune
        TarotCard.STRENGTH -> Res.drawable.tarot_strength
        TarotCard.THE_HANGED_MAN -> Res.drawable.tarot_the_hanged_man
        TarotCard.DEATH -> Res.drawable.tarot_death
        TarotCard.TEMPERANCE -> Res.drawable.tarot_temperance
        TarotCard.THE_DEVIL -> Res.drawable.tarot_the_devil
        TarotCard.THE_TOWER -> Res.drawable.tarot_the_tower
        TarotCard.THE_MOON -> Res.drawable.tarot_the_moon
        TarotCard.THE_SUN -> Res.drawable.tarot_the_sun
        else -> throw IllegalArgumentException("Not implemented card: $card")
    }
}

private fun getTarotCardName(card: TarotCard): StringResource {
    return when (card) {
        TarotCard.THE_FOOL -> Res.string.the_fool
        TarotCard.THE_MAGICIAN -> Res.string.the_magician
        TarotCard.THE_HIGH_PRIESTESS -> Res.string.the_high_priestess
        TarotCard.THE_EMPRESS -> Res.string.the_empress
        TarotCard.THE_EMPEROR -> Res.string.the_emperor
        TarotCard.THE_HIEROPHANT -> Res.string.the_hierophant
        TarotCard.THE_LOVERS -> Res.string.the_lovers
        TarotCard.THE_CHARIOT -> Res.string.the_chariot
        TarotCard.JUSTICE -> Res.string.justice
        TarotCard.THE_HERMIT -> Res.string.the_hermit
        TarotCard.WHEEL_OF_FORTUNE -> Res.string.wheel_of_fortune
        TarotCard.STRENGTH -> Res.string.strength
        TarotCard.THE_HANGED_MAN -> Res.string.the_hanged_man
        TarotCard.DEATH -> Res.string.death
        TarotCard.TEMPERANCE -> Res.string.temperance
        TarotCard.THE_DEVIL -> Res.string.the_devil
        TarotCard.THE_TOWER -> Res.string.the_tower
        TarotCard.THE_MOON -> Res.string.the_moon
        TarotCard.THE_SUN -> Res.string.the_sun
        else -> throw IllegalArgumentException("Not implemented card: $card")
    }
}