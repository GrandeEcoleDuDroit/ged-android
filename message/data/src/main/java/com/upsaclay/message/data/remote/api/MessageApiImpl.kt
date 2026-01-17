package com.upsaclay.message.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.upsaclay.common.data.remote.model.ServerResponse
import com.upsaclay.common.data.utils.sendServerRequest
import com.upsaclay.message.data.mapper.toMap
import com.upsaclay.message.data.model.ConversationField
import com.upsaclay.message.data.model.MessageField.MESSAGE_TABLE_NAME
import com.upsaclay.message.data.model.MessageField.Remote.SEEN
import com.upsaclay.message.data.remote.model.RemoteMessage
import com.upsaclay.message.data.remote.model.RemoteMessageReport
import com.upsaclay.message.data.remote.withOffsetTime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal class MessageApiImpl(private val messageServerApi: MessageServerApi): MessageApi {
    private val conversationsCollection = Firebase.firestore.collection(ConversationField.CONVERSATION_TABLE_NAME)

    override fun listenMessages(conversationId: String, interlocutorId: String, offsetTime: Timestamp?): Flow<RemoteMessage> = callbackFlow {
        val listener = conversationsCollection
            .document(conversationId)
            .collection(MESSAGE_TABLE_NAME)
            .withOffsetTime(offsetTime)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
                error?.let {
                    close(it)
                    return@addSnapshotListener
                }

                snapshot?.documents
                    ?.filterNot { it.metadata.isFromCache || it.metadata.hasPendingWrites() }
                    ?.forEach { document ->
                        document.toObject(RemoteMessage::class.java)?.let {
                            trySend(it)
                        }
                    }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun createMessage(remoteMessage: RemoteMessage) {
        conversationsCollection
            .document(remoteMessage.conversationId)
            .collection(MESSAGE_TABLE_NAME)
            .document(remoteMessage.messageId)
            .set(remoteMessage.toMap(), SetOptions.merge())
            .await()
    }

    override suspend fun setMessageSeen(conversationId: String, messageId: String) {
        conversationsCollection
            .document(conversationId)
            .collection(MESSAGE_TABLE_NAME)
            .document(messageId)
            .update(SEEN, true)
            .await()
    }

    override suspend fun reportMessage(report: RemoteMessageReport) {
        sendServerRequest {
            messageServerApi.reportMessage(report)
        }
    }
}

internal interface MessageServerApi {
    @POST("messages/report")
    suspend fun reportMessage(@Body report: RemoteMessageReport): Response<ServerResponse>
}