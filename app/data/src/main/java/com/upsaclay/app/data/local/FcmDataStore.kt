package com.upsaclay.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsaclay.common.data.extensions.getGsonValue
import com.upsaclay.common.data.extensions.removeValue
import com.upsaclay.common.data.extensions.setGsonValue
import com.upsaclay.common.domain.entity.FcmToken

class FcmDataStore(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "credentials")
    private val store = context.dataStore
    private val fcmTokenKey = stringPreferencesKey("fcmTokenKey")

    suspend fun getFcmToken(): FcmToken? = store.getGsonValue(fcmTokenKey)

    suspend fun storeFcmToken(fcmToken: FcmToken) {
        store.setGsonValue(fcmTokenKey, fcmToken)
    }

    suspend fun deleteFcmToken() {
        store.removeValue(fcmTokenKey)
    }
}