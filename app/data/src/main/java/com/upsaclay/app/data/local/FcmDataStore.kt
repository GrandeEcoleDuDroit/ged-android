package com.upsaclay.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsaclay.app.domain.entity.FcmToken
import com.upsaclay.common.data.extensions.getGsonValue
import com.upsaclay.common.data.extensions.setGsonValue

class FcmDataStore(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "credentials")
    private val store = context.dataStore
    private val fcmTokenKey = stringPreferencesKey("fcmTokenKey")

    suspend fun getFcmToken(): FcmToken? = store.getGsonValue(fcmTokenKey)

    suspend fun storeFcmToken(fcmToken: FcmToken) {
        store.setGsonValue(fcmTokenKey, fcmToken)
    }

    suspend fun removeFcmToken() {
        store.edit { it.remove(fcmTokenKey) }
    }
}