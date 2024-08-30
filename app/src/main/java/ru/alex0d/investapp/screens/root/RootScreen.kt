package ru.alex0d.investapp.screens.root

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.NavGraphs
import org.koin.androidx.compose.koinViewModel
import ru.alex0d.investapp.ui.composables.ConnectivityStatus
import ru.alex0d.investapp.utils.connectivity.ConnectivityObserver

@Composable
fun RootScreen() {
    val viewModel = koinViewModel<RootViewModel>()
    val networkStatus by viewModel.networkStatus.collectAsState()
    val isConnected = networkStatus == ConnectivityObserver.Status.AVAILABLE

    Column {
        ConnectivityStatus(
            isConnected = isConnected,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(8.dp),
        )
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            modifier = if (!isConnected) Modifier.consumeWindowInsets(WindowInsets.statusBars) else Modifier
        )
    }
}