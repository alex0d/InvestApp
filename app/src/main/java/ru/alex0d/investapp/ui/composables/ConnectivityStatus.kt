package ru.alex0d.investapp.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ru.alex0d.investapp.R
import kotlin.time.Duration.Companion.seconds

@Composable
fun ConnectivityStatus(
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {
    var visibility by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = visibility,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        ConnectivityStatusBox(isConnected = isConnected, modifier = modifier)
    }

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            visibility = true
        } else {
            delay(1.seconds)
            visibility = false
        }
    }
}

@Composable
private fun ConnectivityStatusBox(
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isConnected) Color.Green else Color.Red,
        label = "Connectivity Status Background Color"
    )

    val message = if (isConnected) stringResource(R.string.connectivity_available)
        else stringResource(R.string.connectivity_unavailable)

    val iconResource = if (isConnected) {
        R.drawable.ic_connectivity_available
    } else {
        R.drawable.ic_connectivity_unavailable
    }

    Box(
        modifier = Modifier.background(backgroundColor) then modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = iconResource),
                contentDescription = stringResource(R.string.connectivity_icon_description),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = message, color = Color.White, fontSize = 16.sp)
        }
    }
}