package com.upsaclay.message.data.remote.api

import com.google.firebase.Timestamp
import com.upsaclay.message.data.remote.model.RemoteMessage
import com.upsaclay.message.data.remote.model.RemoteMessageReport
import kotlinx.coroutines.flow.Flow

internal interface MessageApi {
    fun listenMessages(conversationId: String, interlocutorId: String, offsetTime: Timestamp?): Flow<RemoteMessage>

    suspend fun createMessage(remoteMessage: RemoteMessage)

    suspend fun setMessageSeen(conversationId: String, messageId: String)

    suspend fun updateMessageVisibility(remoteMessage: RemoteMessage, userId: String, visible: Boolean)

    suspend fun reportMessage(report: RemoteMessageReport)
}