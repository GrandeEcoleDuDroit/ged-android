package com.upsaclay.message.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.upsaclay.common.utils.e
import com.upsaclay.message.data.remote.DataField
import com.upsaclay.message.data.remote.DataField.Conversation.CONVERSATION_ID
import com.upsaclay.message.data.remote.TableName
import com.upsaclay.message.data.remote.model.RemoteMessage
import com.upsaclay.message.domain.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

//Fields
private const val MESSAGE_ID = "message_id"
private const val CONVERSATION_ID = "conversation_id"
private const val SENDER_ID = "sender_id"
private const val TEXT = "text"
private const val TIMESTAMP = "timestamp"
private const val IS_READ = "is_read"
private const val TYPE = "type"

class MessageApiImpl: MessageApi {
    private val messagesCollection = Firebase.firestore.collection(TableName.MESSAGES.value)

    override fun getLastMessages(conversationId: String): Flow<List<RemoteMessage>> = callbackFlow {
        messagesCollection.whereEqualTo(CONVERSATION_ID, conversationId)
            .orderBy(TIMESTAMP)
            .limit(10)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    e("Error getting last messages", it)
                    close(it)
                    throw it
                }

                val messages = snapshot?.toObjects(Message::class.java) ?: emptyList()
                trySend(messages)
            }
    }

    override fun getMessages(conversationId: String, start: Int, end: Int): List<Message> {
        messagesCollection.whereEqualTo(CONVERSATION_ID, conversationId)
            .startAfter(start)
            .orderBy()
            .limit(10)
            .get()
    }
}