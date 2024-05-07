package ru.alex0d.investapp.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.generated.destinations.MainScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>(start = true)
@Composable
fun LoginScreen(
    navigator: DestinationsNavigator
) {
    Scaffold { innerPadding ->
        LoginContent(
            Modifier.padding(innerPadding), navigator
        )
    }
}

@Composable
private fun LoginContent(
    modifier: Modifier, navigator: DestinationsNavigator
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = {
            navigator.navigate(MainScreenDestination()) {
                popUpTo(LoginScreenDestination) {
                    inclusive = true
                }
            }

        }) {
            Text("Login")
        }
    }
}