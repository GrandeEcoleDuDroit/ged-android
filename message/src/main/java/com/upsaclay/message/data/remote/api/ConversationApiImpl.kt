package com.upsaclay.message.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.upsaclay.common.utils.e
import com.upsaclay.message.data.remote.RemoteConversation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class ConversationApiImpl: ConversationApi {
    private val conversations = Firebase.firestore.collection("conversations")

    override suspend fun getConversation(conversationId: String): RemoteConversation? =
        suspendCoroutine { continuation ->
            conversations.document(conversationId).get()
                .addOnSuccessListener {
                    continuation.resume(it.toObject(RemoteConversation::class.java))
                }
                .addOnFailureListener { e ->
                    e("Error getting conversation: ${e.message}", e)
                    continuation.resumeWithException(e)
                }
        }

    override suspend fun getAllConversations(userId: String): List<RemoteConversation> =
        suspendCoroutine { continuation ->
            conversations.whereArrayContains("participants", userId).get()
                .addOnSuccessListener {
                    continuation.resume(it.toObjects(RemoteConversation::class.java))
                }
                .addOnFailureListener { e ->
                    e("Error getting all conversations: ${e.message}", e)
                    continuation.resumeWithException(e)
                }
        }

    override fun createConversation(conversation: RemoteConversation) {
        conversations.add(conversation)
    }
}