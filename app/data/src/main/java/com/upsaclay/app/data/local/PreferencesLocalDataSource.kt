package com.upsaclay.app.data.local

import com.upsaclay.app.domain.entity.NotificationPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PreferencesLocalDataSource(
    private val preferencesDataStore: PreferencesDataStore
) {
    suspend fun getNotificationPreferences(): NotificationPreferences? =
        preferencesDataStore.getNotificationPreferences()

    suspend fun storeNotificationPreferences(notificationPreferences: NotificationPreferences) {
        withContext(Dispatchers.IO) {
            preferencesDataStore.storeNotificationPreferences(notificationPreferences)
        }
    }
}