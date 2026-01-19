package com.upsaclay.common.presentation

import android.os.Bundle

interface NotificationManager {
    fun createNotificationChannel()

    fun presentNotification(extra: Bundle)

    fun onNotificationClick(extra: Bundle)
}