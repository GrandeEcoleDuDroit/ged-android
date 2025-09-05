package com.upsaclay.common.domain.entity.fcm

import com.google.gson.annotations.SerializedName

data class AndroidConfig(
    val priority: AndroidMessagePriority = AndroidMessagePriority.HIGH,
    val notification: AndroidNotification
)

data class AndroidNotification(
    val channelId: String
) {
    val icon: String = "ic_notification"
}

enum class AndroidMessagePriority {
    @SerializedName("high")
    HIGH;

    override fun toString(): String {
        return when (this) {
            HIGH -> "high"
        }
    }
}