package com.upsaclay.authentication.data.remote.google

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

interface GoogleAuthenticationApi {
    suspend fun loginIn(): GoogleIdTokenCredential
}