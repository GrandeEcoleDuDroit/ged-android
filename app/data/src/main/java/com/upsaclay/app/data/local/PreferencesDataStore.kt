package com.upsaclay.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.upsaclay.app.domain.entity.NotificationPreferences
import com.upsaclay.common.data.extensions.getGsonValue
import com.upsaclay.common.data.extensions.setGsonValue

class PreferencesDataStore(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userPreferences")
    private val store = context.dataStore
    private val notificationPreferencesKey = stringPreferencesKey("notificationPreferencesKey")

    suspend fun getNotificationPreferences(): NotificationPreferences? =
        store.getGsonValue(notificationPreferencesKey)

    suspend fun storeNotificationPreferences(notificationPreferences: NotificationPreferences) {
        store.setGsonValue(notificationPreferencesKey, notificationPreferences)
    }
}