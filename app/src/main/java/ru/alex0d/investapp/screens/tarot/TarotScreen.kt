package ru.alex0d.investapp.screens.tarot

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import ru.alex0d.investapp.R
import ru.alex0d.investapp.domain.models.TarotCard
import ru.alex0d.investapp.utils.MainGraph

@OptIn(ExperimentalMaterial3Api::class)
@Destination<MainGraph>
@Composable
fun TarotScreen(
    navigator: DestinationsNavigator,
    stockName: String = ""
) {
    val viewModel: TarotViewModel = getViewModel { parametersOf(stockName) }
    val state = viewModel.state.collectAsState().value

    Scaffold(
        topBar = {
            if (state is TarotPredictionState.Success) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navigator.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                tint = MaterialTheme.colorScheme.primary,
                                contentDescription = stringResource(R.string.go_back)
                            )
                        }
                    },
                    title = {
                        Text(
                            text = stringResource(R.string.esoteric_analysis),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .padding(horizontal = 2.dp)) {
            when (state) {
                is TarotPredictionState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                is TarotPredictionState.Error -> Text(state.message)
                is TarotPredictionState.Success -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        item {
                            Text(
                                modifier = Modifier.padding(vertical = 20.dp),
                                text = stockName,
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 32.sp,
                            )
                            Image(
                                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                                painter = painterResource(id = getTarotImage(state.prediction.card)),
                                contentDescription = ""
                            )
                            Text(
                                modifier = Modifier.padding(vertical = 16.dp),
                                text = stringResource(id = getTarotCardName(state.prediction.card)),
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 24.sp,
                            )
                            Text(
                                text = state.prediction.prediction,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }

                }
            }
        }
    }
}

@DrawableRes
private fun getTarotImage(card: TarotCard): Int {
    return when (card) {
        TarotCard.THE_FOOL -> R.drawable.tarot_the_fool
        TarotCard.THE_MAGICIAN -> R.drawable.tarot_the_magician
        TarotCard.THE_HIGH_PRIESTESS -> R.drawable.tarot_the_high_priestess
        TarotCard.THE_EMPRESS -> R.drawable.tarot_the_empress
        TarotCard.THE_EMPEROR -> R.drawable.tarot_the_emperor
        TarotCard.THE_HIEROPHANT -> R.drawable.tarot_the_hierophant
        TarotCard.THE_LOVERS -> R.drawable.tarot_the_lovers
        TarotCard.THE_CHARIOT -> R.drawable.tarot_the_chariot
        TarotCard.JUSTICE -> R.drawable.tarot_justice
        TarotCard.THE_HERMIT -> R.drawable.tarot_the_hermit
        TarotCard.WHEEL_OF_FORTUNE -> R.drawable.tarot_wheel_of_fortune
        TarotCard.STRENGTH -> R.drawable.tarot_strength
        TarotCard.THE_HANGED_MAN -> R.drawable.tarot_the_hanged_man
        TarotCard.DEATH -> R.drawable.tarot_death
        TarotCard.TEMPERANCE -> R.drawable.tarot_temperance
        TarotCard.THE_DEVIL -> R.drawable.tarot_the_devil
        TarotCard.THE_TOWER -> R.drawable.tarot_the_tower
        TarotCard.THE_MOON -> R.drawable.tarot_the_moon
        TarotCard.THE_SUN -> R.drawable.tarot_the_sun
        else -> throw IllegalArgumentException("Not implemented card: $card")
    }
}

@StringRes
private fun getTarotCardName(card: TarotCard): Int {
    return when (card) {
        TarotCard.THE_FOOL -> R.string.the_fool
        TarotCard.THE_MAGICIAN -> R.string.the_magician
        TarotCard.THE_HIGH_PRIESTESS -> R.string.the_high_priestess
        TarotCard.THE_EMPRESS -> R.string.the_empress
        TarotCard.THE_EMPEROR -> R.string.the_emperor
        TarotCard.THE_HIEROPHANT -> R.string.the_hierophant
        TarotCard.THE_LOVERS -> R.string.the_lovers
        TarotCard.THE_CHARIOT -> R.string.the_chariot
        TarotCard.JUSTICE -> R.string.justice
        TarotCard.THE_HERMIT -> R.string.the_hermit
        TarotCard.WHEEL_OF_FORTUNE -> R.string.wheel_of_fortune
        TarotCard.STRENGTH -> R.string.strength
        TarotCard.THE_HANGED_MAN -> R.string.the_hanged_man
        TarotCard.DEATH -> R.string.death
        TarotCard.TEMPERANCE -> R.string.temperance
        TarotCard.THE_DEVIL -> R.string.the_devil
        TarotCard.THE_TOWER -> R.string.the_tower
        TarotCard.THE_MOON -> R.string.the_moon
        TarotCard.THE_SUN -> R.string.the_sun
        else -> throw IllegalArgumentException("Not implemented card: $card")
    }
}