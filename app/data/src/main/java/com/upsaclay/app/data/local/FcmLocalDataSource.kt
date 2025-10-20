package com.upsaclay.app.data.local

import com.upsaclay.app.domain.entity.FcmToken

class FcmLocalDataSource(
    private val fcmDataStore: FcmDataStore
) {
    suspend fun getUnsentFcmToken(): FcmToken? = fcmDataStore.getFcmToken()

    suspend fun storeUnsentFcmToken(fcmToken: FcmToken) {
        fcmDataStore.storeFcmToken(fcmToken)
    }

    suspend fun removeUnsentFcmToken() {
        fcmDataStore.removeFcmToken()
    }
}