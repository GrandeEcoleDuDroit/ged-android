package com.upsaclay.authentication.data.model

sealed class AuthTokenState {
    data class Valid(val token: String): AuthTokenState()
    data object Unauthenticated: AuthTokenState()
    data class Error(val throwable: Throwable?): AuthTokenState()
}
