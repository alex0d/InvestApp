package ru.alex0d.investapp.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.destinations.MainScreenDestination
import com.ramcosta.composedestinations.generated.navgraphs.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import ru.alex0d.investapp.utils.MainGraph

@Destination<MainGraph>
@Composable
fun ProfileScreen(
    rootNavigator: DestinationsNavigator,
    viewModel: ProfileViewModel = koinViewModel()
) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .statusBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            viewModel.logout()
            rootNavigator.navigate(RootNavGraph) {
                popUpTo(MainScreenDestination) {
                    inclusive = true
                }
            }
        }) {
            Text(
                text = "Logout"
            )
        }
    }
}