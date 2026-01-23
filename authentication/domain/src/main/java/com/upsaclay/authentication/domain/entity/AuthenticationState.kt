package com.upsaclay.authentication.domain.entity

sealed class AuthenticationState {
    data class Authenticated(val userId: String): AuthenticationState() {
        companion object {
            const val TYPE = "AUTHENTICATED"
        }
    }
    data object Unauthenticated: AuthenticationState() {
        const val TYPE = "UNAUTHENTICATED"
    }
}