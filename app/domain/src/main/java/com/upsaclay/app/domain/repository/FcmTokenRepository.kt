package com.upsaclay.app.domain.repository

import com.upsaclay.app.domain.entity.FcmToken


interface FcmTokenRepository {
    suspend fun generateToken(): String

    suspend fun getUnsentFcmToken(): FcmToken?

    suspend fun sendFcmToken(token: FcmToken)

    suspend fun storeUnsentFcmToken(token: FcmToken)

    suspend fun removeUnsentFcmToken()

    fun deleteToken()
}