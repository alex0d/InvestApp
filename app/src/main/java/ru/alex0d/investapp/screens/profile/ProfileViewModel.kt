package ru.alex0d.investapp.screens.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import ru.alex0d.investapp.data.JwtDataStore

class ProfileViewModel(
    private val jwtDataStore: JwtDataStore
) : ViewModel() {
    fun logout() {
        runBlocking { jwtDataStore.clear() }
    }
}