package ru.alex0d.investapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val USER_PREFS_NAME = "user_prefs"
val Context.dataStore by preferencesDataStore(name = USER_PREFS_NAME)

class UserDataStore(private val context: Context) {

    suspend fun saveUserDetails(accessToken: String, refreshToken: String, firstname: String, lastname: String?, email: String) {
        saveAccessToken(accessToken)
        saveRefreshToken(refreshToken)
        saveUserFirstname(firstname)
        saveUserLastname(lastname)
        saveUserEmail(email)
    }

    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { it[ACCESS_TOKEN_KEY] = token }
    }

    suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { it[REFRESH_TOKEN_KEY] = token }
    }

    suspend fun saveUserFirstname(firstname: String) {
        context.dataStore.edit { it[USER_FIRSTNAME_KEY] = firstname }
    }

    suspend fun saveUserLastname(lastname: String?) {
        lastname?.let {
            context.dataStore.edit { it[USER_LASTNAME_KEY] = lastname }
        }
    }

    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { it[USER_EMAIL_KEY] = email }
    }

    val accessToken: Flow<String?>
        get() = context.dataStore.data.map { it[ACCESS_TOKEN_KEY] }

    val refreshToken: Flow<String?>
        get() = context.dataStore.data.map { it[REFRESH_TOKEN_KEY] }

    val userFirstname: Flow<String?>
        get() = context.dataStore.data.map { it[USER_FIRSTNAME_KEY] }

    val userLastname: Flow<String?>
        get() = context.dataStore.data.map { it[USER_LASTNAME_KEY] }

    val userEmail: Flow<String?>
        get() = context.dataStore.data.map { it[USER_EMAIL_KEY] }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

        private val USER_FIRSTNAME_KEY = stringPreferencesKey("user_firstname")
        private val USER_LASTNAME_KEY = stringPreferencesKey("user_lastname")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }
}