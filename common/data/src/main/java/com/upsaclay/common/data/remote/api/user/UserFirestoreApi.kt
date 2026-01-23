package com.upsaclay.common.data.remote.api.user

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.upsaclay.common.data.UserField.Firestore.TABLE_NAME
import com.upsaclay.common.data.remote.model.FirestoreUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal class UserFirestoreApi {
    private val usersCollection = Firebase.firestore.collection(TABLE_NAME)

    fun listenUser(userId: String): Flow<FirestoreUser?> = callbackFlow {
        val listener = usersCollection.document(userId)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    close(it)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    val user = it.toObject(FirestoreUser::class.java)
                    trySend(user)
                }
            }

        awaitClose { listener.remove() }
    }
}