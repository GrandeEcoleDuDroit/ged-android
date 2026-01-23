package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.entity.FcmToken

interface FcmTokenRepository {
    suspend fun generateToken(): String

    suspend fun getFcmToken(): FcmToken?

    suspend fun sendFcmToken(userId: String, token: String)

    suspend fun storeFcmToken(fcmToken: FcmToken)

    suspend fun deleteToken(userId: String)
}