package com.upsaclay.message.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.upsaclay.common.utils.e
import com.upsaclay.common.utils.i
import com.upsaclay.message.data.remote.model.RemoteConversation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ConversationApiImpl: ConversationApi {
    private val conversationsCollection = Firebase.firestore.collection("conversations")
    private val lastConversations = emptyList<RemoteConversation>()

    override fun getAllConversations(userId: String): Flow<List<RemoteConversation>> = callbackFlow {
        val listener = conversationsCollection.whereArrayContains("user_id", userId)
            .addSnapshotListener { value, error ->
                error?.let {
                    e("Error getting conversations", it)
                    close(it)
                    throw it
                }
                var conversations = value?.let {
                    when (it.size()) {
                        0 -> emptyList()
                        1 -> {
                            val conversation = it.documents.first().toObject(RemoteConversation::class.java)
                            if(conversation != null) listOf(conversation) else emptyList()
                        }
                        else -> it.toObjects(RemoteConversation::class.java)
                    }
                } ?: emptyList()

                conversations = conversations.filter { remoteConversation ->
                    lastConversations.none { it == remoteConversation }
                }
                trySend(conversations)
            }
        awaitClose { listener.remove() }
    }

    override fun updateConversation(remoteConversation: RemoteConversation) {
        conversationsCollection.document(remoteConversation.conversationId)
            .set(remoteConversation, SetOptions.merge())
            .addOnSuccessListener {
                i("Conversation updated successfully")
            }
            .addOnFailureListener { e ->
                e("Error updating conversations", e)
                throw e
            }
    }

    override suspend fun createConversation(remoteConversation: RemoteConversation): String {
        return suspendCoroutine { continuation ->
            conversationsCollection.add(remoteConversation)
                .addOnSuccessListener {
                    i("Conversation created successfully")
                    continuation.resume(it.id)
                }
                .addOnFailureListener { e ->
                    e("Error creating conversations", e)
                    continuation.resumeWithException(e)
                }
        }
    }
}