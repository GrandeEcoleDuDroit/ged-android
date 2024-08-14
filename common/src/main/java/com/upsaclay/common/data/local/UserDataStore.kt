package com.upsaclay.common.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.utils.getGsonValue
import com.upsaclay.common.utils.getValue
import com.upsaclay.common.utils.removeValue
import com.upsaclay.common.utils.setGsonValue
import com.upsaclay.common.utils.setValue

class UserDataStore(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
    private val store = context.dataStore
    private val userKey = stringPreferencesKey("userKey")
    private val hasDefaultProfilePictureKey = booleanPreferencesKey("hasDefaultProfilePictureKey")

    suspend fun storeCurrentUser(user: UserDTO){
        store.setGsonValue(userKey, user)
    }

    suspend fun getCurrentUser(): UserDTO? {
        return store.getGsonValue(userKey)
    }

    suspend fun removeCurrentUser(){
        store.removeValue(userKey)
    }

    suspend fun getUserHasDefaultProfilePicture(): Boolean {
        return store.getValue(hasDefaultProfilePictureKey) ?: false
    }

    suspend fun storeUserHasDefaultProfilePicture(hasDefaultProfilePicture: Boolean){
        store.setValue(hasDefaultProfilePictureKey, hasDefaultProfilePicture)
    }
}