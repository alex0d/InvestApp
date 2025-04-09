package ru.alex0d.investapp.screens.root

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import ru.alex0d.investapp.screens.auth.AuthScreen

@Composable
fun RootScreen() {
    // TODO: Implement Connectivity Observer

    Navigator(AuthScreen())
}