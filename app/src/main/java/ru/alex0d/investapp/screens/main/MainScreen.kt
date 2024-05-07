package ru.alex0d.investapp.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.NavGraphs
import ru.alex0d.investapp.ui.composables.BottomBar

@Destination<RootGraph>
@Composable
fun MainScreen(
) {
    val navController = rememberNavController()

    Scaffold(bottomBar = {
        BottomBar(navController = navController)
    }) { innerPadding ->
        DestinationsNavHost(
            modifier = Modifier.padding(innerPadding),
            navGraph = NavGraphs.main,
            navController = navController
        )
    }
}