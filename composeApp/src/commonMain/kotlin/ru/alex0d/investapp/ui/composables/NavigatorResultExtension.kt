package ru.alex0d.investapp.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.lifecycle.NavigatorDisposable
import cafe.adriel.voyager.navigator.lifecycle.NavigatorLifecycleStore

val Navigator.navigationResult: VoyagerResultExtension
    @Composable get() = remember {
        NavigatorLifecycleStore.get(this) {
            VoyagerResultExtension(this)
        }
    }

class VoyagerResultExtension(
    private val navigator: Navigator
) : NavigatorDisposable {
    private val results = mutableStateMapOf<String, Any?>()
    private val lasResultKey = mutableStateOf<String?>(null)

    override fun onDispose(navigator: Navigator) {
        // no-op
    }

    fun setResult(screenKey: String, result: Any?) {
        results[screenKey] = result
        lasResultKey.value = screenKey
    }

    fun popWithResult(result: Any? = null) {
        val currentScreen = navigator.lastItem
        results[currentScreen.key] = result
        lasResultKey.value = currentScreen.key
        navigator.pop()
    }

    fun clearResults() {
        results.clear()
        lasResultKey.value = null
    }

    fun popUntilWithResult(predicate: (Screen) -> Boolean, result: Any? = null) {
        val currentScreen = navigator.lastItem
        results[currentScreen.key] = result
        lasResultKey.value = currentScreen.key
        navigator.popUntil(predicate)
    }

    @Suppress("UNCHECKED_CAST")
    @Composable
    fun <T> getResult(screenKey: String): State<T?> {
        val result = results[screenKey] as? T
        val resultState = remember(screenKey, result) {
            derivedStateOf {
                results.remove(screenKey)
                lasResultKey.value = null
                result
            }
        }
        return resultState
    }

    @Suppress("UNCHECKED_CAST")
    @Composable
    fun <T> getLastResult(): State<T?> {
        val result = results[lasResultKey.value] as? T
        val resultState = remember(result) {
            derivedStateOf {
                results.remove(lasResultKey.value)
                lasResultKey.value = null
                result
            }
        }
        return resultState
    }
}
