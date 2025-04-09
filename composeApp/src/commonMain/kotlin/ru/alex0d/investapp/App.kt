package ru.alex0d.investapp

import androidx.compose.runtime.Composable
import ru.alex0d.investapp.screens.root.RootScreen
import ru.alex0d.investapp.ui.theme.InvestAppTheme

@Composable
fun App() {
    InvestAppTheme {
        RootScreen()
    }
}