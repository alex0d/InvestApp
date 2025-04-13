package ru.alex0d.investapp.data.local

import kotlinx.coroutines.flow.Flow

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class UserDataStore {
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
    val userFirstname: Flow<String?>
    val userLastname: Flow<String?>
    val userEmail: Flow<String?>

    suspend fun saveUserDetails(
        accessToken: String,
        refreshToken: String,
        firstname: String,
        lastname: String?,
        email: String
    )
    suspend fun saveAccessToken(token: String)
    suspend fun saveRefreshToken(token: String)
    suspend fun saveUserFirstname(firstname: String)
    suspend fun saveUserLastname(lastname: String?)
    suspend fun saveUserEmail(email: String)
    suspend fun clear()
}