package com.upsaclay.app.data.repository

import com.upsaclay.app.data.local.PreferencesLocalDataSource
import com.upsaclay.app.domain.entity.NotificationPreferences
import com.upsaclay.app.domain.repository.PreferencesRepository

class PreferencesRepositoryImpl(
    private val preferencesLocalDataSource: PreferencesLocalDataSource
): PreferencesRepository {
    override suspend fun getNotificationPreferences(): NotificationPreferences? =
        preferencesLocalDataSource.getNotificationPreferences()

    override suspend fun storeNotificationPreferences(notificationPreferences: NotificationPreferences) {
        preferencesLocalDataSource.storeNotificationPreferences(notificationPreferences)
    }
}