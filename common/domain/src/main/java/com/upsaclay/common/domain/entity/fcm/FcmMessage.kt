package com.upsaclay.common.domain.entity.fcm

data class FcmMessage<T>(
    val data: FcmData<T>,
    val android: AndroidConfig,
    val apns: ApnsConfig
)