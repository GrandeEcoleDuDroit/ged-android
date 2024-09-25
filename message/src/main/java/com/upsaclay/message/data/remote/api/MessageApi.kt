package com.upsaclay.message.data.remote.api

import com.upsaclay.message.data.remote.model.RemoteMessage
import kotlinx.coroutines.flow.Flow

interface MessageApi {
    fun getLastMessages(conversationId: String): Flow<List<RemoteMessage>>

    fun getMessages(conversationId: String, start: Int): List<RemoteMessage>
}