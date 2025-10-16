package com.upsaclay.authentication.data.api

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.upsaclay.common.domain.entity.CurrentUserNotFoundException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthenticationApiImpl: FirebaseAuthenticationApi {
    private val firebaseAuth = Firebase.auth
    private var cachedIdToken: String? = null

    init {
        refreshAndCacheToken()
    }

    override fun isAuthenticated(): Boolean = firebaseAuth.currentUser != null

    override fun listenAuthenticationState(): Flow<Boolean> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }

        firebaseAuth.addAuthStateListener(listener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }
    override fun getIdToken(): String? = cachedIdToken

    override suspend fun signIn(email: String, password: String) {
        suspendCancellableCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { if (continuation.isActive) continuation.resume(Unit) }
                .addOnFailureListener { if (continuation.isActive) continuation.resumeWithException(it) }
        }
    }

    override suspend fun signUp(email: String, password: String): String = suspendCancellableCoroutine { continuation ->
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { continuation.resume(it.user!!.uid) }
            .addOnFailureListener { continuation.resumeWithException(it) }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun deleteAuthUser() {
        firebaseAuth.currentUser?.delete() ?: throw CurrentUserNotFoundException()
    }

    private fun refreshAndCacheToken() {
        firebaseAuth.addIdTokenListener(FirebaseAuth.IdTokenListener { auth ->
            auth.currentUser?.getIdToken(false)?.addOnSuccessListener { result ->
                cachedIdToken = result.token
            }
        })
    }
}