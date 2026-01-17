package com.upsaclay.message.data.remote

import com.upsaclay.common.data.exceptions.mapFirebaseException
import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.message.data.mapper.toRemote
import com.upsaclay.message.data.model.ConversationField.Remote.EFFECTIVE_FROM
import com.upsaclay.message.data.remote.api.ConversationApi
import com.upsaclay.message.data.remote.model.RemoteConversation
import com.upsaclay.message.domain.entity.Conversation
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

internal class ConversationRemoteDataSource(private val conversationApi: ConversationApi) {
    fun listenConversations(userId: String): Flow<RemoteConversation> =
        conversationApi.listenConversations(userId)

    suspend fun createConversation(conversation: Conversation, userId: String) {
        try {
            conversationApi.createConversation(conversation.toRemote(userId))
        } catch (e: Exception) {
            throw mapFirebaseException(e)
        }
    }

    suspend fun updateConversationEffectiveFrom(conversationId: String, currentUserId: String, dateTime: LocalDateTime) {
        try {
            val data = mapOf("$EFFECTIVE_FROM.$currentUserId" to dateTime.toTimestamp())
            conversationApi.updateConversation(conversationId, data)
        } catch (e: Exception) {
            throw mapFirebaseException(e)
        }
    }
}