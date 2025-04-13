package ru.alex0d.investapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class UserDataStore(
    private val dataStore: DataStore<Preferences>
) {

    actual suspend fun saveUserDetails(accessToken: String, refreshToken: String, firstname: String, lastname: String?, email: String) {
        saveAccessToken(accessToken)
        saveRefreshToken(refreshToken)
        saveUserFirstname(firstname)
        saveUserLastname(lastname)
        saveUserEmail(email)
    }

    actual suspend fun saveAccessToken(token: String) {
        dataStore.edit { it[ACCESS_TOKEN_KEY] = token }
    }

    actual suspend fun saveRefreshToken(token: String) {
        dataStore.edit { it[REFRESH_TOKEN_KEY] = token }
    }

    actual suspend fun saveUserFirstname(firstname: String) {
        dataStore.edit { it[USER_FIRSTNAME_KEY] = firstname }
    }

    actual suspend fun saveUserLastname(lastname: String?) {
        lastname?.let {
            dataStore.edit { it[USER_LASTNAME_KEY] = lastname }
        }
    }

    actual suspend fun saveUserEmail(email: String) {
        dataStore.edit { it[USER_EMAIL_KEY] = email }
    }

    actual val accessToken: Flow<String?>
        get() = dataStore.data.map { it[ACCESS_TOKEN_KEY] }

    actual val refreshToken: Flow<String?>
        get() = dataStore.data.map { it[REFRESH_TOKEN_KEY] }

    actual val userFirstname: Flow<String?>
        get() = dataStore.data.map { it[USER_FIRSTNAME_KEY] }

    actual val userLastname: Flow<String?>
        get() = dataStore.data.map { it[USER_LASTNAME_KEY] }

    actual val userEmail: Flow<String?>
        get() = dataStore.data.map { it[USER_EMAIL_KEY] }

    actual suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")

        private val USER_FIRSTNAME_KEY = stringPreferencesKey("user_firstname")
        private val USER_LASTNAME_KEY = stringPreferencesKey("user_lastname")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }
}