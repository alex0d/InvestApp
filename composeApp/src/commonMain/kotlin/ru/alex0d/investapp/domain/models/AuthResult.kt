package ru.alex0d.investapp.domain.models

import ru.alex0d.investapp.utils.AuroraExport

@AuroraExport
enum class AuthResult {
    SUCCESS,
    USER_NOT_FOUND,
    EMAIL_ALREADY_REGISTERED,
    INVALID_CREDENTIALS,
    UNKNOWN_ERROR
}