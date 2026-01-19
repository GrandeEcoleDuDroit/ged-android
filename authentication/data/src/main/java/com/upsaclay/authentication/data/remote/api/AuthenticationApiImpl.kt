package com.upsaclay.authentication.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.upsaclay.authentication.data.model.AuthTokenState
import com.upsaclay.authentication.domain.entity.AuthenticationState
import com.upsaclay.common.domain.entity.CustomException
import com.upsaclay.common.domain.entity.CustomException.CustomError.CURRENT_USER_NOT_FOUND
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AuthenticationApiImpl: AuthenticationApi {
    private val firebaseAuth = Firebase.auth

    override fun listenAuthenticationState(): Flow<AuthenticationState> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            auth.currentUser?.uid?.let {
                trySend(AuthenticationState.Authenticated(it))
            } ?: trySend(AuthenticationState.Unauthenticated)
        }

        firebaseAuth.addAuthStateListener(listener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }

    override fun listenAuthTokenState(): Flow<AuthTokenState> = callbackFlow {
        val listener = FirebaseAuth.IdTokenListener { auth ->
            val user = auth.currentUser

            if (user == null) {
                trySend(AuthTokenState.Unauthenticated)
                return@IdTokenListener
            }

            user.getIdToken(true)
                .addOnSuccessListener { result ->
                    result.token?.let {
                        trySend(AuthTokenState.Valid(it))
                    } ?: run {
                        trySend(AuthTokenState.Error(null))
                    }
                }
                .addOnFailureListener {
                    trySend(AuthTokenState.Error(it))
                }
        }

        firebaseAuth.addIdTokenListener(listener)

        awaitClose {
            firebaseAuth.removeIdTokenListener(listener)
        }
    }

    override fun isAuthenticated(): Boolean = firebaseAuth.currentUser != null

    override suspend fun signIn(email: String, password: String): String? =
        firebaseAuth.signInWithEmailAndPassword(email, password).await().user?.uid

    override suspend fun signUp(email: String, password: String): String? =
        firebaseAuth.createUserWithEmailAndPassword(email, password).await().user?.uid

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun deleteAuthUser() {
        firebaseAuth.currentUser?.delete() ?: throw CustomException(CURRENT_USER_NOT_FOUND, Exception())
    }
}