package ru.alex0d.investapp.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

// TODO: Implement
@Composable
actual fun keyboardAsState(): State<Keyboard> = remember { mutableStateOf(Keyboard.CLOSED) }