package com.upsaclay.message.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class StartupMessageWorker(context: Context) {
    private val workerManager = WorkManager.getInstance(context)

    fun run() {
        workerManager.enqueue(sendUnsentConversationWorkRequest())
        workerManager.enqueue(sendUnsentMessageWorkRequest())
    }

    private fun sendUnsentMessageWorkRequest(): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return OneTimeWorkRequestBuilder<SendUnsentMessageWorker>()
            .setConstraints(constraints)
            .build()
    }

    private fun sendUnsentConversationWorkRequest(): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return OneTimeWorkRequestBuilder<SendUnsentConversationWorker>()
            .setConstraints(constraints)
            .build()
    }
}