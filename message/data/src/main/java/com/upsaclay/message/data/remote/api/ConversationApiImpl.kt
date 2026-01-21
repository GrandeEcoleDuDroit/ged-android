package com.upsaclay.message.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.upsaclay.message.data.mapper.toMap
import com.upsaclay.message.data.model.ConversationField
import com.upsaclay.message.data.model.ConversationField.CONVERSATION_TABLE_NAME
import com.upsaclay.message.data.remote.model.RemoteConversation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

internal class ConversationApiImpl: ConversationApi {
    private val conversationsCollection = Firebase.firestore.collection(CONVERSATION_TABLE_NAME)

    override fun listenConversations(userId: String): Flow<RemoteConversation> = callbackFlow {
        val listener = conversationsCollection
            .whereArrayContains(ConversationField.Remote.PARTICIPANTS, userId)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
                error?.let {
                    close(it)
                    return@addSnapshotListener
                }

                snapshot?.documents
                    ?.filter { !it.metadata.isFromCache && !it.metadata.hasPendingWrites() }
                    ?.forEach { document ->
                        document.toObject(RemoteConversation::class.java)?.let {
                            trySend(it)
                        }
                    }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun createConversation(remoteConversation: RemoteConversation) {
        val conversationExist = conversationsCollection.document(remoteConversation.conversationId).get().await().exists()
        if (!conversationExist) {
            conversationsCollection
                .document(remoteConversation.conversationId)
                .set(remoteConversation.toMap(), SetOptions.merge())
                .await()
        }
    }

    override suspend fun updateConversation(conversationId: String, data: Map<String, Any>) {
        conversationsCollection
            .document(conversationId)
            .update(data)
            .await()
    }
}