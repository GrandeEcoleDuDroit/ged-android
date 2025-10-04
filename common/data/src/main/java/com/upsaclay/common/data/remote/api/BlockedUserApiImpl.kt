package com.upsaclay.common.data.remote.api

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.upsaclay.common.data.exceptions.mapFirebaseException
import kotlinx.coroutines.tasks.await

internal class BlockedUserApiImpl(
    private val blockedUserFirestoreApi: BlockedUserFirestoreApi
): BlockedUserApi {
    override suspend fun getBlockedUserIds(currentUserId: String): Set<String> {
        return mapFirebaseException(
            block = { blockedUserFirestoreApi.getBlockedUserIds(currentUserId) },
            message = "Failed to get blocked user ids with Firestore"
        )
    }

    override suspend fun blockUser(currentUserId: String, userId: String) {
        mapFirebaseException(
            block = { blockedUserFirestoreApi.blockUser(currentUserId, userId) },
            message = "Failed to block user with Firestore"
        )
    }

    override suspend fun unblockUser(currentUserId: String, userId: String) {
        mapFirebaseException(
            block = { blockedUserFirestoreApi.unblockUser(currentUserId, userId) },
            message = "Failed to unblock user with Firestore"
        )
    }
}

internal class BlockedUserFirestoreApi {
    private val blockedUsersCollection = Firebase.firestore.collection("blockedUsers")
    private val dataKey = "userIds"

    suspend fun getBlockedUserIds(currentUserId: String): Set<String> {
        val userIds = blockedUsersCollection.document(currentUserId)
            .get()
            .await()
            .get(dataKey) as? List<*>

        return userIds?.mapNotNull { it as? String }
            ?.toSet()
            ?: emptySet()
    }

    suspend fun blockUser(currentUserId: String, blockedUserId: String) {
        val data = hashMapOf(dataKey to FieldValue.arrayUnion(blockedUserId))

        blockedUsersCollection.document(currentUserId)
            .set(data, SetOptions.merge())
            .await()
    }

    suspend fun unblockUser(currentUserId: String, blockedUserId: String) {
        val data = hashMapOf(dataKey to FieldValue.arrayRemove(blockedUserId))

        blockedUsersCollection.document(currentUserId)
            .set(data, SetOptions.merge())
            .await()
    }
}