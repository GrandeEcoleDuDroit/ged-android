package com.upsaclay.app.domain.repository

import com.upsaclay.app.domain.entity.NotificationPreferences

interface PreferencesRepository {
    suspend fun getNotificationPreferences(): NotificationPreferences?

    suspend fun storeNotificationPreferences(notificationPreferences: NotificationPreferences)
}