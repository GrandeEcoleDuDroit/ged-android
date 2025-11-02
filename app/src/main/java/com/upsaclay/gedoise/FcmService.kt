package com.upsaclay.gedoise

import android.content.Intent
import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.gson.Gson
import com.upsaclay.app.domain.FcmTokenUseCase
import com.upsaclay.app.domain.entity.FcmToken
import com.upsaclay.common.domain.entity.fcm.FcmDataType
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.data.mapper.toMessageNotification
import com.upsaclay.message.data.remote.RemoteMessageNotification
import com.upsaclay.message.notification.MessageNotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FcmService: FirebaseMessagingService() {
    private var job: Job? = null
    private val messageNotificationManager: MessageNotificationManager by inject<MessageNotificationManager>()
    private val fcmTokenUseCase: FcmTokenUseCase by inject<FcmTokenUseCase>()
    private val userRepository: UserRepository by inject<UserRepository>()
    private val scope = CoroutineScope(SupervisorJob())
    private val gson = Gson()

    override fun onNewToken(tokenValue: String) {
        super.onNewToken(tokenValue)
        job?.cancel()
        job = scope.launch(Dispatchers.IO) {
            userRepository.user
                .take(1)
                .collect {
                    fcmTokenUseCase.sendFcmToken(FcmToken(it.id, tokenValue))
                }
        }
    }

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
        scope.launch(Dispatchers.Main) {
            when (intent?.extras?.getString("type")) {
                FcmDataType.MESSAGE.toString() -> showMessageNotification(intent.extras)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private suspend fun showMessageNotification(extra: Bundle?) {
        extra?.getString("value")?.let { value ->
            val messageNotification = gson.fromJson(value, RemoteMessageNotification::class.java).toMessageNotification()
            messageNotificationManager.showNotification(messageNotification)
        }
    }
}