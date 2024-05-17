package ru.alex0d.investapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val JWT_PREFS_NAME = "jwt_prefs"
val Context.dataStore by preferencesDataStore(name = JWT_PREFS_NAME)

class JwtDataStore(private val context: Context) {
    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { it[ACCESS_TOKEN_KEY] = token }
    }

    suspend fun saveRefreshToken(token: String) {
        context.dataStore.edit { it[REFRESH_TOKEN_KEY] = token }
    }

    val accessToken: Flow<String?>
        get() = context.dataStore.data.map { it[ACCESS_TOKEN_KEY] }

    val refreshToken: Flow<String?>
        get() = context.dataStore.data.map { it[REFRESH_TOKEN_KEY] }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }
}