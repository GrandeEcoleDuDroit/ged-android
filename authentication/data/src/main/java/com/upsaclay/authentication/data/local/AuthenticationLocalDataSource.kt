package com.upsaclay.authentication.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.GsonBuilder
import com.upsaclay.authentication.data.AuthenticationStateAdapter
import com.upsaclay.authentication.domain.entity.AuthenticationState
import com.upsaclay.common.data.extensions.getGsonFlowValue
import com.upsaclay.common.data.extensions.getGsonValue
import com.upsaclay.common.data.extensions.setGsonValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class AuthenticationLocalDataSource(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")
    private val store = context.dataStore
    private val authenticationStateKey = stringPreferencesKey("authenticationStateKey")
    private val gson = GsonBuilder()
        .registerTypeHierarchyAdapter(AuthenticationState::class.java, AuthenticationStateAdapter)
        .create()

    fun listenAuthenticationState(): Flow<AuthenticationState> =
        store.getGsonFlowValue<AuthenticationState>(authenticationStateKey, gson)
            .map { it ?: AuthenticationState.Unauthenticated }

    suspend fun getAuthenticationState(): AuthenticationState? = withContext(Dispatchers.IO) {
        store.getGsonValue(authenticationStateKey, gson)
    }

    suspend fun storeAuthenticationState(authenticationState: AuthenticationState) = withContext(Dispatchers.IO) {
        store.setGsonValue(authenticationStateKey, authenticationState, gson)
    }
}