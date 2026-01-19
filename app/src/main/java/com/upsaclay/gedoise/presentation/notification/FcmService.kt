package com.upsaclay.gedoise.presentation.notification

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.upsaclay.app.domain.usecase.FcmTokenUseCase
import org.koin.android.ext.android.inject

class FcmService: FirebaseMessagingService() {
    private val fcmTokenUseCase: FcmTokenUseCase by inject<FcmTokenUseCase>()
    private val notificationMediator: NotificationMediator by inject<NotificationMediator>()

    override fun onNewToken(tokenValue: String) {
        super.onNewToken(tokenValue)
        fcmTokenUseCase.onNewTokenReceived(tokenValue)
    }

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
        intent?.extras?.let(notificationMediator::presentNotification)
    }
}