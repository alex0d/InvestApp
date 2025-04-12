package ru.alex0d.investapp.data.local

import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class UserDataStore {
    private val _accessToken = MutableStateFlow<String?>(null)
    private val _refreshToken = MutableStateFlow<String?>(null)
    private val _userFirstname = MutableStateFlow<String?>(null)
    private val _userLastname = MutableStateFlow<String?>(null)
    private val _userEmail = MutableStateFlow<String?>(null)

    actual suspend fun saveUserDetails(
        accessToken: String,
        refreshToken: String,
        firstname: String,
        lastname: String?,
        email: String
    ) {
        saveAccessToken(accessToken)
        saveRefreshToken(refreshToken)
        saveUserFirstname(firstname)
        saveUserLastname(lastname)
        saveUserEmail(email)
    }

    init {
        _accessToken.value = localStorage.getItem("accessToken")
        _refreshToken.value = localStorage.getItem("refreshToken")
        _userFirstname.value = localStorage.getItem("userFirstname")
        _userLastname.value = localStorage.getItem("userLastname")
        _userEmail.value = localStorage.getItem("userEmail")
    }

    actual suspend fun saveAccessToken(token: String) {
        localStorage.setItem("accessToken", token)
        _accessToken.value = token
    }

    actual suspend fun saveRefreshToken(token: String) {
        localStorage.setItem("refreshToken", token)
        _refreshToken.value = token
    }

    actual suspend fun saveUserFirstname(firstname: String) {
        localStorage.setItem("userFirstname", firstname)
        _userFirstname.value = firstname
    }

    actual suspend fun saveUserLastname(lastname: String?) {
        localStorage.setItem("userLastname", lastname ?: "")
        _userLastname.value = lastname
    }

    actual suspend fun saveUserEmail(email: String) {
        localStorage.setItem("userEmail", email)
        _userEmail.value = email
    }

    actual suspend fun clear() {
        localStorage.clear()
        _accessToken.value = null
        _refreshToken.value = null
        _userFirstname.value = null
        _userLastname.value = null
        _userEmail.value = null
    }

    actual val accessToken: Flow<String?> = _accessToken.asStateFlow()

    actual val refreshToken: Flow<String?> = _refreshToken.asStateFlow()

    actual val userFirstname: Flow<String?> = _userFirstname.asStateFlow()

    actual val userLastname: Flow<String?> = _userLastname.asStateFlow()

    actual val userEmail: Flow<String?> = _userEmail.asStateFlow()
}