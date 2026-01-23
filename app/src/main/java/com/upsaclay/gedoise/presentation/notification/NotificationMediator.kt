package com.upsaclay.gedoise.presentation.notification

import android.os.Bundle
import com.upsaclay.common.domain.entity.fcm.FcmDataType
import com.upsaclay.message.notification.MessageNotificationManager

class NotificationMediator(
    private val messageNotificationManager: MessageNotificationManager
) {
    fun createNotificationChannels() {
        messageNotificationManager.createNotificationChannel()
    }

    fun presentNotification(extra: Bundle) {
        when(getType(extra)) {
            FcmDataType.MESSAGE -> messageNotificationManager.presentNotification(extra)
            else -> Unit
        }
    }

    fun onNotificationClick(extra: Bundle) {
        when (getType(extra)) {
            FcmDataType.MESSAGE -> messageNotificationManager.onNotificationClick(extra)
            else -> Unit
        }
    }

    private fun getType(extra: Bundle): FcmDataType? =
        extra.getString("type")?.let(FcmDataType::fromString)
}