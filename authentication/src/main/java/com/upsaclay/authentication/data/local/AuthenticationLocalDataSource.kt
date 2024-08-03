package com.upsaclay.authentication.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsaclay.common.utils.getValue
import com.upsaclay.common.utils.setValue

class AuthenticationLocalDataSource(
    context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")
    private val store = context.dataStore
    private val authenticationKey = booleanPreferencesKey("authenticationKey")

    suspend fun setAuthenticated() {
        store.setValue(authenticationKey, true)
    }

    suspend fun setUnauthenticated() {
        store.setValue(authenticationKey, false)
    }

    suspend fun isAuthenticated(): Boolean = store.getValue(authenticationKey) ?: false
}