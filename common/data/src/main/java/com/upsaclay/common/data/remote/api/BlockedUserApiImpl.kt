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

    override suspend fun blockUser(currentUserId: String, blockedUserId: String) {
        mapFirebaseException(
            block = { blockedUserFirestoreApi.blockUser(currentUserId, blockedUserId) },
            message = "Failed to block user with Firestore"
        )
    }

    override suspend fun unblockUser(currentUserId: String, blockedUserId: String) {
        mapFirebaseException(
            block = { blockedUserFirestoreApi.unblockUser(currentUserId, blockedUserId) },
            message = "Failed to unblock user with Firestore"
        )
    }
}

internal class BlockedUserFirestoreApi {
    private val tableName = "blockedUsers"
    private val documentName = "userIds"
    private val blockedUsersCollection = Firebase.firestore.collection(tableName)

    suspend fun getBlockedUserIds(currentUserId: String): Set<String> {
        val userIds = blockedUsersCollection.document(currentUserId)
            .get()
            .await()
            .get(documentName, ArrayList::class.java)

        return userIds?.mapNotNull { it as? String }?.toSet() ?: emptySet()
    }

    suspend fun blockUser(currentUserId: String, blockedUserId: String) {
        val data = hashMapOf(documentName to FieldValue.arrayUnion(blockedUserId))

        blockedUsersCollection.document(currentUserId)
            .set(data, SetOptions.merge())
            .await()
    }

    suspend fun unblockUser(currentUserId: String, blockedUserId: String) {
        val data = hashMapOf(documentName to FieldValue.arrayRemove(blockedUserId))

        blockedUsersCollection.document(currentUserId)
            .set(data, SetOptions.merge())
            .await()
    }
}