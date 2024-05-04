package ru.alex0d.investapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import ru.alex0d.investapp.screens.portfolio.PortfolioScreen
import ru.alex0d.investapp.ui.theme.InvestAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InvestAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PortfolioScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}