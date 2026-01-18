package com.upsaclay.app.data.local

import com.upsaclay.common.domain.entity.FcmToken

class FcmLocalDataSource(private val fcmDataStore: FcmDataStore) {
    suspend fun getFcmToken(): FcmToken? = fcmDataStore.getFcmToken()

    suspend fun storeFcmToken(fcmToken: FcmToken) {
        fcmDataStore.storeFcmToken(fcmToken)
    }

    suspend fun deleteFcmToken() {
        fcmDataStore.deleteFcmToken()
    }
}