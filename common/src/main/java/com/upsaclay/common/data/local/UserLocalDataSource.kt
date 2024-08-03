package com.upsaclay.common.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.utils.getGsonValue
import com.upsaclay.common.utils.removeValue
import com.upsaclay.common.utils.setGsonValue

class UserLocalDataSource(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
    private val store = context.dataStore
    private val userKey = stringPreferencesKey("userKey")

    suspend fun storeCurrentUser(user: UserDTO){
        store.setGsonValue(userKey, user)
    }

    suspend fun getCurrentUser(): UserDTO? {
        return store.getGsonValue(userKey)
    }

    suspend fun removeCurrentUser(){
        store.removeValue(userKey)
    }
}