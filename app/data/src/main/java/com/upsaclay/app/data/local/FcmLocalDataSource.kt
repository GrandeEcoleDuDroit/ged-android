package com.upsaclay.app.data.local

import com.upsaclay.app.domain.entity.FcmToken

class FcmLocalDataSource(private val fcmDataStore: FcmDataStore) {
    suspend fun getFcmToken(): FcmToken? = fcmDataStore.getFcmToken()

    suspend fun storeFcmToken(fcmToken: FcmToken) {
        fcmDataStore.storeFcmToken(fcmToken)
    }
}