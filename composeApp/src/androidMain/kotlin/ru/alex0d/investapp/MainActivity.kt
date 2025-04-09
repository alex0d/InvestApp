package ru.alex0d.investapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.koin.android.ext.koin.androidContext
import ru.alex0d.investapp.di.nativeModule
import ru.alex0d.investapp.di.startDI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startDI(nativeModule) { androidContext(this@MainActivity) }

        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}
