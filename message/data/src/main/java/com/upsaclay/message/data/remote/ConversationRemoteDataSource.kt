package com.upsaclay.message.data.remote

import com.upsaclay.common.data.exceptions.mapFirebaseException
import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.message.data.mapper.toMap
import com.upsaclay.message.data.mapper.toRemote
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_DELETE_TIME
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
            val data = conversation.toRemote(userId).toMap()
            conversationApi.createConversation(conversation.id, data)
        } catch (e: Exception) {
            throw mapFirebaseException(e)
        }
    }

    suspend fun updateConversationDeleteTime(conversationId: String, currentUserId: String, deleteTIme: LocalDateTime) {
        try {
            val data = mapOf("$CONVERSATION_DELETE_TIME.$currentUserId" to deleteTIme.toTimestamp())
            conversationApi.updateConversation(conversationId, data)
        } catch (e: Exception) {
            throw mapFirebaseException(e)
        }
    }
}