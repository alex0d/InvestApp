package ru.alex0d.investapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.alex0d.investapp.screens.root.RootScreen
import ru.alex0d.investapp.ui.theme.InvestAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InvestAppTheme {
                RootScreen()
            }
        }
    }
}