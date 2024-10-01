package com.upsaclay.message.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.upsaclay.common.utils.e
import com.upsaclay.message.data.model.CONVERSATIONS_TABLE_NAME
import com.upsaclay.message.data.model.MESSAGES_TABLE_NAME
import com.upsaclay.message.data.remote.MessageField.TIMESTAMP
import com.upsaclay.message.data.remote.model.RemoteMessage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class MessageApiImpl: MessageApi {
    private val conversationCollection = Firebase.firestore.collection(CONVERSATIONS_TABLE_NAME)

    override fun listenLastMessage(conversationId: String): Flow<RemoteMessage> = callbackFlow {
        val listener = conversationCollection.document(conversationId)
            .collection(MESSAGES_TABLE_NAME)
            .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
            .limit(1)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    e("Error getting last messages", it)
                    close(it)
                }

                snapshot?.toObjects(RemoteMessage::class.java)?.firstOrNull()?.let { trySend(it) }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getMessages(conversationId: String, limit: Long): List<RemoteMessage> =
        suspendCoroutine { continuation ->
            conversationCollection.document(conversationId)
                .collection(MESSAGES_TABLE_NAME)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .addOnSuccessListener {
                    val messages = it.toObjects(RemoteMessage::class.java)
                    continuation.resume(messages)
                }
                .addOnFailureListener { e ->
                    e("Error getting messages", e)
                    continuation.resume(emptyList())
                }
    }
}