package com.upsaclay.authentication.domain.model

internal enum class AuthenticationState {
    AUTHENTICATED,
    UNAUTHENTICATED,
    AUTHENTICATION_ERROR,
    INPUT_ERROR,
    LOADING
}