package com.upsaclay.authentication.data.remote.google

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.i
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleAuthenticationRemoteDataSource(
    private val googleAuthenticationApi: GoogleAuthenticationApi
) {
    suspend fun login(): GoogleIdTokenCredential? = withContext(Dispatchers.IO) {
        i("Logging in with Google...")
        try {
            googleAuthenticationApi.loginIn()
        } catch (e: Exception) {
            e("Error logging in with Google: ${e.message}")
            null
        }
    }
}