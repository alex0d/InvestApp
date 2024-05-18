package ru.alex0d.investapp.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.alex0d.investapp.data.repositories.AuthRepository
import ru.alex0d.investapp.domain.models.AuthResult

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _authErrorState = MutableStateFlow<AuthErrorState>(AuthErrorState.None)
    val authErrorState: StateFlow<AuthErrorState> = _authErrorState.asStateFlow()

    var firstname by mutableStateOf("")
        private set
    var isValidFirstname by mutableStateOf(false)
        private set

    var lastname by mutableStateOf("")
        private set
    var isValidLastname by mutableStateOf(true)
        private set

    var email by mutableStateOf("")
        private set
    var isValidEmail by mutableStateOf(false)
        private set

    var password by mutableStateOf("")
        private set
    var isValidPassword by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            val authResult = authRepository.authenticateByTokensInDataBase()
            if (authResult == AuthResult.SUCCESS) {
                _authState.value = AuthState.Success
            }
        }
    }

    fun updateFirstname(value: String) {
        if (value.length > 40) return
        firstname = value
        isValidFirstname = Regex("^[A-ZА-Я ]{1,40}\$", RegexOption.IGNORE_CASE).matches(value)
    }

    fun updateLastname(value: String) {
        if (value.length > 40) return
        lastname = value
        isValidLastname = Regex("^[A-ZА-Я ]{0,40}\$", RegexOption.IGNORE_CASE).matches(value)
    }

    fun updateEmail(value: String) {
        email = value
        isValidEmail = PatternsCompat.EMAIL_ADDRESS.matcher(value).matches()
    }

    fun updatePassword(value: String) {
        if (value.length > 32) return
        password = value
        isValidPassword = Regex("^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*\\W*)(?!.*\\s).{8,32}\$").matches(value)
    }

    fun clearError() {
        _authErrorState.value = AuthErrorState.None
    }

    fun register() {
        if (!(isValidFirstname && isValidLastname && isValidEmail && isValidPassword)) return

        firstname = firstname.trim()
        val userLastname = if (lastname.isNotBlank()) lastname.trim() else null

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val authResult = authRepository.register(firstname, userLastname, email, password)
            if (authResult == AuthResult.SUCCESS) {
                _authState.value = AuthState.Success
            } else {
                _authErrorState.value = AuthErrorState.Error(authResult)
                _authState.value = AuthState.Idle
            }
        }
    }

    fun authenticate() {
        if (!(isValidEmail && isValidPassword)) return

        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val authResult = authRepository.authenticate(email, password)
            if (authResult == AuthResult.SUCCESS) {
                _authState.value = AuthState.Success
            } else {
                _authErrorState.value = AuthErrorState.Error(authResult)
                _authState.value = AuthState.Idle
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
}

sealed class AuthErrorState {
    object None : AuthErrorState()
    data class Error(val result: AuthResult) : AuthErrorState()
}
