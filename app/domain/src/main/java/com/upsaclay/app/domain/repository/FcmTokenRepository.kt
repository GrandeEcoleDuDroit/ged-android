package com.upsaclay.app.domain.repository

import com.upsaclay.app.domain.entity.FcmToken

interface FcmTokenRepository {
    suspend fun generateToken(): String

    suspend fun getFcmToken(): FcmToken?

    suspend fun sendFcmToken(userId: String, token: String)

    suspend fun storeFcmToken(fcmToken: FcmToken)
}