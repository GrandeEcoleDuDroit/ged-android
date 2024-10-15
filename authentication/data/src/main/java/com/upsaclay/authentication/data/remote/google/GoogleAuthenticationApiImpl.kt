package com.upsaclay.authentication.data.remote.google

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import java.security.MessageDigest
import java.util.UUID

private const val WEB_CLIENT_ID = "954607401960-cpqdush7169cccuc9f1rtakt39i008bg.apps.googleusercontent.com"

class GoogleAuthenticationApiImpl(
    private val context: Context
): GoogleAuthenticationApi {
    private val credentialManager = CredentialManager.create(context)

    override suspend fun loginIn(): GoogleIdTokenCredential {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(WEB_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .setNonce(generateNonce())
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            context = context,
            request = request
        )

        return GoogleIdTokenCredential.createFrom(result.credential.data)
    }

    private fun generateNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(bytes)
        val hashedNonce = digest.fold("", { str, it -> str + "%02x".format(it) })
        return hashedNonce
    }
}