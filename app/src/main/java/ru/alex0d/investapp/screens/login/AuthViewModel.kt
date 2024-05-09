package ru.alex0d.investapp.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.alex0d.investapp.data.repositories.AuthRepository
import ru.alex0d.investapp.data.JwtDataStore
import ru.alex0d.investapp.data.remote.models.AuthRequest
import ru.alex0d.investapp.data.remote.models.RegisterRequest

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val jwtDataStore: JwtDataStore
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            val accessToken = jwtDataStore.accessToken.first()
            val refreshToken = jwtDataStore.refreshToken.first()
            if (!accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                _authState.value = AuthState.Success(accessToken, refreshToken)
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authRepository.register(request)
                jwtDataStore.saveAccessToken(response.accessToken)
                jwtDataStore.saveRefreshToken(response.refreshToken)
                _authState.value = AuthState.Success(response.accessToken, response.refreshToken)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun authenticate(request: AuthRequest) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authRepository.authenticate(request)
                jwtDataStore.saveAccessToken(response.accessToken)
                jwtDataStore.saveRefreshToken(response.refreshToken)
                _authState.value = AuthState.Success(response.accessToken, response.refreshToken)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val accessToken: String, val refreshToken: String) : AuthState()
    data class Error(val message: String) : AuthState()
}