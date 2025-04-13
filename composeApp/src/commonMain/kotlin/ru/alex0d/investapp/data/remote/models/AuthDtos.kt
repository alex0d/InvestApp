package ru.alex0d.investapp.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val firstname: String,
    val lastname: String? = null,
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val firstname: String,
    val lastname: String? = null,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)

@Serializable
data class RefreshRequest(
    val refreshToken: String
)