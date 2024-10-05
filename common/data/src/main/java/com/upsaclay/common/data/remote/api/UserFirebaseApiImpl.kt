package com.upsaclay.common.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class UserFirebaseApiImpl : UserFirebaseApi {
    private val users = Firebase.firestore.collection("users")

    override suspend fun getUser(userId: String): RemoteUserFirebase? = suspendCoroutine { continuation ->
        users.document(userId).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(RemoteUserFirebase::class.java)
                continuation.resume(user)
            }
            .addOnFailureListener { e ->
                continuation.resumeWithException(e)
            }
    }

    override suspend fun getAllUsers(): List<RemoteUserFirebase> = suspendCoroutine { continuation ->
        users.get()
            .addOnSuccessListener { querySnapshot ->
                val allUsers = querySnapshot.documents.mapNotNull {
                    it.toObject(
                        RemoteUserFirebase::class.java
                    )
                }
                continuation.resume(allUsers)
            }
            .addOnFailureListener { e ->
                continuation.resumeWithException(e)
            }
    }

    override suspend fun getOnlineUsers(): List<RemoteUserFirebase> = suspendCoroutine { continuation ->
        users.whereEqualTo("is_online", true).get()
            .addOnSuccessListener { querySnapshot ->
                val allOnlineUsers = querySnapshot.documents.mapNotNull {
                    it.toObject(
                        RemoteUserFirebase::class.java
                    )
                }
                continuation.resume(allOnlineUsers)
            }
            .addOnFailureListener { e ->
                continuation.resumeWithException(e)
            }
    }

    override suspend fun createUser(remoteUserFirebase: RemoteUserFirebase): Result<Unit> = suspendCoroutine { continuation ->
        users.document(remoteUserFirebase.userId.toString()).set(remoteUserFirebase)
            .addOnSuccessListener {
                continuation.resume(Result.success(Unit))
            }
            .addOnFailureListener { e ->
                continuation.resumeWithException(e)
            }
    }

    override suspend fun updateProfilePictureUrl(userId: String, profilePictureUrl: String?): Result<Unit> =
        suspendCoroutine { continuation ->
            users.document(userId).update("profile_picture_url", profilePictureUrl)
                .addOnSuccessListener {
                    continuation.resume(Result.success(Unit))
                }
                .addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
        }
}