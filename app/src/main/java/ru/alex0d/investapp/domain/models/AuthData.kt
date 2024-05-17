package ru.alex0d.investapp.domain.models

data class AuthData(
    val result: AuthResult,
    val accessToken: String? = null,
    val refreshToken: String? = null
)

enum class AuthResult {
    SUCCESS,
    USER_NOT_FOUND,
    EMAIL_ALREADY_REGISTERED,
    INVALID_CREDENTIALS,
    UNKNOWN_ERROR
}