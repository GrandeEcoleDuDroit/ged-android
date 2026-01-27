package com.upsaclay.authentication.data.remote

import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.upsaclay.authentication.data.model.AuthTokenState
import com.upsaclay.authentication.data.remote.api.AuthenticationApi
import com.upsaclay.authentication.domain.entity.AuthenticationException
import com.upsaclay.authentication.domain.entity.AuthenticationException.AuthenticationError.EMAIL_ALREADY_IN_USE
import com.upsaclay.authentication.domain.entity.AuthenticationException.AuthenticationError.INVALID_CREDENTIALS
import com.upsaclay.authentication.domain.entity.AuthenticationException.AuthenticationError.USER_DISABLED
import com.upsaclay.authentication.domain.entity.AuthenticationState
import com.upsaclay.common.data.exceptions.mapFirebaseException
import com.upsaclay.common.domain.entity.CustomException
import com.upsaclay.common.domain.entity.CustomException.CustomError.FORBIDDEN
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AuthenticationRemoteDataSource(private val authenticationApi: AuthenticationApi) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun listenAuthenticationState(): Flow<AuthenticationState> = authenticationApi.listenAuthenticationState()

    fun listenAuthTokenState(): Flow<AuthTokenState> = authenticationApi.listenAuthTokenState()

    fun isAuthenticated(): Boolean = authenticationApi.isAuthenticated()

    suspend fun getAuthToken(): String? = authenticationApi.getAuthToken()

    suspend fun loginWithEmailAndPassword(email: String, password: String): String? = withContext(dispatcher) {
        try {
            authenticationApi.signIn(email, password)
        } catch (e: Exception) {
            throw mapFirebaseAuthException(e)
        }
    }

    suspend fun registerWithEmailAndPassword(email: String, password: String): String? = withContext(dispatcher) {
        try {
            authenticationApi.signUp(email, password)
        } catch (e: Exception) {
            throw mapFirebaseAuthException(e)
        }
    }

    fun logout() {
        authenticationApi.signOut()
    }

    private fun mapFirebaseAuthException(e: Exception): Exception {
        return when(e) {
            is FirebaseAuthInvalidCredentialsException -> AuthenticationException(INVALID_CREDENTIALS, e)
            is FirebaseAuthInvalidUserException -> mapFirebaseAuthErrorCode(e)
            is FirebaseAuthUserCollisionException -> AuthenticationException(EMAIL_ALREADY_IN_USE, e)
            is FirebaseAuthException -> mapFirebaseAuthErrorCode(e)
            else -> mapFirebaseException(e)
        }
    }

    private fun mapFirebaseAuthErrorCode(e: FirebaseAuthException): Exception {
        return when (e.errorCode) {
            "ERROR_EMAIL_ALREADY_IN_USE" -> AuthenticationException(EMAIL_ALREADY_IN_USE, e)
            "ERROR_USER_NOT_FOUND" -> AuthenticationException(INVALID_CREDENTIALS, e)
            "ERROR_USER_DISABLED" -> AuthenticationException(USER_DISABLED, e)
            "ERROR_ADMIN_RESTRICTED_OPERATION" -> CustomException(FORBIDDEN, e)
            else -> mapFirebaseException(e)
        }
    }

    fun forgotPassword(email: String) {4
        try {
            authenticationApi.forgotPassword(email)
        } catch (e : Exception){
            throw mapFirebaseAuthException(e)
        }
    }
}