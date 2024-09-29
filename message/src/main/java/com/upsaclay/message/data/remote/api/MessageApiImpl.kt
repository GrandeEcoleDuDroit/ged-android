package com.upsaclay.message.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.upsaclay.common.utils.e
import com.upsaclay.message.data.model.MESSAGES_TABLE_NAME
import com.upsaclay.message.data.remote.MessageField.CONVERSATION_ID
import com.upsaclay.message.data.remote.MessageField.TIMESTAMP
import com.upsaclay.message.data.remote.model.RemoteMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class MessageApiImpl: MessageApi {
    private val messagesCollection = Firebase.firestore.collection(MESSAGES_TABLE_NAME)

    override fun listenLastMessages(conversationId: String): Flow<List<RemoteMessage>> = callbackFlow {
        messagesCollection.whereEqualTo(CONVERSATION_ID, conversationId)
            .orderBy(TIMESTAMP)
            .limit(10)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    e("Error getting last messages", it)
                    close(it)
                    throw it
                }

                val messages = snapshot?.toObjects(RemoteMessage::class.java) ?: emptyList()
                trySend(messages)
            }
    }

    override suspend fun getMessages(conversationId: String, start: Int): List<RemoteMessage> =
        suspendCoroutine { continuation ->
            messagesCollection.whereEqualTo(CONVERSATION_ID, conversationId)
                .startAfter(start)
                .orderBy(TIMESTAMP)
                .limit(10)
                .get()
                .addOnSuccessListener {
                    val messages = it.toObjects(RemoteMessage::class.java)
                    continuation.resume(messages)
                }
                .addOnFailureListener { e ->
                    e("Error getting messages", e)
                    continuation.resumeWithException(e)
                }
    }
}