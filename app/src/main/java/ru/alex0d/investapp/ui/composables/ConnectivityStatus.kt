package ru.alex0d.investapp.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.alex0d.investapp.R

@Composable
fun ConnectivityStatusBox(
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