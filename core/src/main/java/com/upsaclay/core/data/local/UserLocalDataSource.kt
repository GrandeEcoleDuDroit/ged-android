package com.upsaclay.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsaclay.core.data.model.UserDTO
import com.upsaclay.core.utils.getGsonValue
import com.upsaclay.core.utils.removeValue
import com.upsaclay.core.utils.setGsonValue

class UserLocalDataSource(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
    private val store = context.dataStore
    private val userKey = stringPreferencesKey("userKey")

    suspend fun storeUser(user: UserDTO){
        store.setGsonValue(userKey, user)
    }

    suspend fun getUser(): UserDTO? {
        return store.getGsonValue(userKey)
    }

    suspend fun removeUser(){
        store.removeValue(userKey)
    }
}