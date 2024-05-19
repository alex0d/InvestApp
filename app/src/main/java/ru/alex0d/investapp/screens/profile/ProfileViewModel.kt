package ru.alex0d.investapp.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.alex0d.investapp.data.repositories.UserRepository

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state: MutableStateFlow<ProfileState> = _state

    init {
        fetchUserDetails()
    }

    private fun fetchUserDetails() {
        viewModelScope.launch {
            val email = userRepository.getUserEmail()
            val firstname = userRepository.getUserFirstname()
            val lastname = userRepository.getUserLastname()
            if (email != null && firstname != null) {
                _state.value = ProfileState.Success(email, firstname, lastname)
            } else {
                _state.value = ProfileState.Error
            }

        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val email: String, val firstname: String, val lastname: String?): ProfileState()
    object Error : ProfileState()
}