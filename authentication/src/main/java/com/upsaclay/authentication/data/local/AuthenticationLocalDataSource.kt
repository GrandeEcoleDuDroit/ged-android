package com.upsaclay.authentication.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsaclay.common.utils.getValue
import com.upsaclay.common.utils.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationLocalDataSource(
    context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")
    private val store = context.dataStore
    private val authenticationKey = booleanPreferencesKey("authenticationKey")

    suspend fun setAuthenticated() = withContext(Dispatchers.IO) {
        store.setValue(authenticationKey, true)
    }

    suspend fun setUnauthenticated() = withContext(Dispatchers.IO) {
        store.setValue(authenticationKey, false)
    }

    suspend fun isAuthenticated(): Boolean = withContext(Dispatchers.IO) {
        store.getValue(authenticationKey) ?: false
    }
}