package com.upsaclay.authentication.data

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.data.TokenProvider

class TokenProviderImpl(
    private val authenticationRepository: AuthenticationRepository
): TokenProvider {
    override fun getAuthIdToken(): String? = authenticationRepository.authToken
}