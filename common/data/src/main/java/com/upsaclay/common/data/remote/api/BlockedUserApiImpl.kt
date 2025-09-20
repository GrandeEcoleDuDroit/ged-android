package com.upsaclay.common.data.remote.api

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class BlockedUserApiImpl: BlockedUserApi {
    private val tableName = "blockedUsers"
    private val blockedUsersCollection = Firebase.firestore.collection(tableName)

    override suspend fun blockUser(currentUserId: String, blockedUserId: String) {
        val data = hashMapOf<String, Any>(
            "userIds" to FieldValue.arrayUnion(blockedUserId)
        )

        blockedUsersCollection.document(currentUserId)
            .update(data)
            .await()
    }

    override suspend fun unblockUser(currentUserId: String, blockedUserId: String) {
        val data = hashMapOf<String, Any>(
            "userIds" to FieldValue.arrayRemove(blockedUserId)
        )

        blockedUsersCollection.document(currentUserId)
            .update(data)
            .await()
    }
}