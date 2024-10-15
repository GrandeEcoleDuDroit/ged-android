package com.upsaclay.authentication.data.repository

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.upsaclay.authentication.data.remote.google.GoogleAuthenticationRemoteDataSource
import com.upsaclay.authentication.domain.repository.GoogleAuthenticationRepository
import java.io.IOException

class GoogleAuthenticationRepositoryImpl(
    private val googleAuthenticationRemoteDataSource: GoogleAuthenticationRemoteDataSource
): GoogleAuthenticationRepository {
    private var credential: GoogleIdTokenCredential? = null

    override suspend fun login(): Result<String> {
        credential = googleAuthenticationRemoteDataSource.login()
        return credential?.let {
            Result.success(it.id)
        } ?: run {
            Result.failure(IOException("Error logging in with Google"))
        }
    }
}