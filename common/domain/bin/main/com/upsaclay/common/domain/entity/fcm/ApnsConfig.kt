package com.upsaclay.common.domain.entity.fcm

data class ApnsConfig(
    val headers: ApnsHeaders,
    val payload: ApnsPayload
)

data class ApnsHeaders(
    val apnsPushType: String = "alert",
    val apnsPriority: String = "10",
    val apnsCollapseId: String
)

data class ApnsPayload(
    val aps: Aps
)

data class Aps(
    val alert: Alert,
    val sound: String = "default",
    val badge: Int = 1
)

data class Alert(
    val title: String,
    val body: String
)