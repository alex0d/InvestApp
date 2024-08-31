package ru.alex0d.investapp.screens.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.generated.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import ru.alex0d.investapp.screens.profile.ProfileScreen
import ru.alex0d.investapp.ui.composables.BottomBar

@Destination<RootGraph>
@Composable
fun MainScreen(
    rootNavigator: DestinationsNavigator
) {
    val navController = rememberNavController()

    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.statusBars),
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { innerPadding ->
        DestinationsNavHost(
            modifier = Modifier.padding(innerPadding),
            navGraph = NavGraphs.main,
            navController = navController
        ) {
            composable(ProfileScreenDestination) {
                ProfileScreen(
                    rootNavigator = rootNavigator
                )
            }
        }
    }
}