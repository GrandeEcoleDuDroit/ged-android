package com.upsaclay.message.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.i
import com.upsaclay.message.data.model.CONVERSATIONS_TABLE_NAME
import com.upsaclay.message.data.model.ConversationField
import com.upsaclay.message.data.remote.model.RemoteConversation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal class ConversationApiImpl : ConversationApi {
    private val conversationsCollection = Firebase.firestore.collection(CONVERSATIONS_TABLE_NAME)

    override fun listenAllConversations(userId: Int): Flow<List<RemoteConversation>> = callbackFlow {
        val listener = conversationsCollection.whereArrayContains(ConversationField.PARTICIPANTS, userId)
            .addSnapshotListener { value, error ->
                error?.let {
                    e("Error getting conversations", it)
                    close(it)
                }

                val conversations = value?.let {
                    when (it.size()) {
                        0 -> emptyList()
                        1 -> {
                            val conversation = it.documents.first().toObject(RemoteConversation::class.java)
                            if (conversation != null) listOf(conversation) else emptyList()
                        }
                        else -> it.toObjects(RemoteConversation::class.java)
                    }
                } ?: emptyList()

                trySend(conversations)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun createConversation(remoteConversation: RemoteConversation): String = suspendCoroutine { continuation ->
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