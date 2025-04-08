package ru.alex0d.investapp.screens.root

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import ru.alex0d.investapp.screens.auth.AuthScreen
import ru.alex0d.investapp.ui.composables.ConnectivityStatusBox
import ru.alex0d.investapp.utils.connectivity.ConnectivityObserver
import kotlin.time.Duration.Companion.seconds

@Composable
fun RootScreen() {
    val viewModel = koinViewModel<RootViewModel>()
    val networkStatus by viewModel.networkStatus.collectAsState()
    val isConnected = networkStatus == ConnectivityObserver.Status.AVAILABLE

    var connectivityVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(isConnected) {
        if (!isConnected) {
            connectivityVisibility = true
        } else {
            delay(1.seconds)
            connectivityVisibility = false
        }
    }

    Column {
        AnimatedVisibility(
            visible = connectivityVisibility,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            ConnectivityStatusBox(
                isConnected = isConnected,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(8.dp),
            )
        }

        Navigator(AuthScreen())
    }
}