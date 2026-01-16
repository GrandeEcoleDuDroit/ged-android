package com.upsaclay.authentication.data.remote

import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.upsaclay.authentication.data.remote.api.AuthenticationApi
import com.upsaclay.authentication.domain.entity.exception.AuthenticationException
import com.upsaclay.authentication.domain.entity.exception.AuthenticationException.AuthExceptionType.EMAIL_ALREADY_IN_USE_EXCEPTION
import com.upsaclay.authentication.domain.entity.exception.AuthenticationException.AuthExceptionType.INVALID_CREDENTIALS_EXCEPTION
import com.upsaclay.authentication.domain.entity.exception.AuthenticationException.AuthExceptionType.USER_DISABLED_EXCEPTION
import com.upsaclay.common.data.exceptions.mapFirebaseException
import com.upsaclay.common.domain.entity.CustomException
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.FORBIDDEN_EXCEPTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AuthenticationRemoteDataSource(private val authenticationApi: AuthenticationApi) {
    fun isAuthenticated(): Boolean = authenticationApi.isAuthenticated()

    fun listenAuthenticationState(): Flow<Boolean> = authenticationApi.listenAuthenticationState()

    fun getAuthToken(): String? = authenticationApi.getIdToken()

    suspend fun loginWithEmailAndPassword(email: String, password: String): String? = withContext(Dispatchers.IO) {
        try {
            authenticationApi.signIn(email, password)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    suspend fun registerWithEmailAndPassword(email: String, password: String): String = withContext(Dispatchers.IO) {
        try {
            authenticationApi.signUp(email, password)
        } catch (e: Exception) {
            throw mapException(e)
        }
    }

    fun logout() {
        authenticationApi.signOut()
    }

    suspend fun deleteAuthUser() {
        withContext(Dispatchers.IO) {
            try {
                authenticationApi.deleteAuthUser()
            } catch (e: Exception) {
                throw mapException(e)
            }
        }
    }

    private fun mapException(e: Exception): Exception {
        return when(e) {
            is FirebaseAuthInvalidCredentialsException -> AuthenticationException(INVALID_CREDENTIALS_EXCEPTION, e)
            is FirebaseAuthInvalidUserException -> mapErrorCode(e)
            is FirebaseAuthUserCollisionException -> AuthenticationException(EMAIL_ALREADY_IN_USE_EXCEPTION, e)
            is FirebaseAuthException -> mapErrorCode(e)
            else -> mapFirebaseException(e)
        }
    }

    private fun mapErrorCode(e: FirebaseAuthException): Exception {
        return when (e.errorCode) {
            "ERROR_EMAIL_ALREADY_IN_USE" -> AuthenticationException(EMAIL_ALREADY_IN_USE_EXCEPTION, e)
            "ERROR_USER_NOT_FOUND" -> AuthenticationException(INVALID_CREDENTIALS_EXCEPTION, e)
            "ERROR_USER_DISABLED" -> AuthenticationException(USER_DISABLED_EXCEPTION, e)
            "ERROR_ADMIN_RESTRICTED_OPERATION" -> CustomException(FORBIDDEN_EXCEPTION, e)
            else -> mapFirebaseException(e)
        }
    }
}