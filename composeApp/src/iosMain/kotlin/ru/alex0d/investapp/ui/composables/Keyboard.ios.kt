package ru.alex0d.investapp.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIKeyboardDidHideNotification
import platform.UIKit.UIKeyboardDidShowNotification

@Composable
actual fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.CLOSED) }

    DisposableEffect(Unit) {
        val keyboardWillShow = NSNotificationCenter.defaultCenter.addObserverForName(
            UIKeyboardDidShowNotification,
            null,
            NSOperationQueue.mainQueue
        ) { _ ->
            keyboardState.value = Keyboard.OPENED
        }

        val keyboardWillHide = NSNotificationCenter.defaultCenter.addObserverForName(
            UIKeyboardDidHideNotification,
            null,
            NSOperationQueue.mainQueue
        ) { _ ->
            keyboardState.value = Keyboard.CLOSED
        }

        onDispose {
            NSNotificationCenter.defaultCenter.removeObserver(keyboardWillShow)
            NSNotificationCenter.defaultCenter.removeObserver(keyboardWillHide)
        }
    }

    return keyboardState
}
