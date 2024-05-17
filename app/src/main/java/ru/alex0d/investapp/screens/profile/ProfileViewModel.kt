package ru.alex0d.investapp.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.alex0d.investapp.data.repositories.AuthRepository

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}