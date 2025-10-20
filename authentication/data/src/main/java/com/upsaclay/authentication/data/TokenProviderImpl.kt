package com.upsaclay.authentication.data

import com.upsaclay.authentication.data.repository.firebase.FirebaseAuthenticationRepository
import com.upsaclay.common.data.TokenProvider

class TokenProviderImpl(
    private val firebaseAuthenticationRepository: FirebaseAuthenticationRepository
): TokenProvider {
    override fun getAuthIdToken(): String? = firebaseAuthenticationRepository.getIdToken()
}