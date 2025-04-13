package ru.alex0d.investapp.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

enum class Keyboard {
    OPENED,
    CLOSED
}

@Composable
expect fun keyboardAsState(): State<Keyboard>
