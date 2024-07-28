package com.upsaclay.authentication.domain.model

enum class AuthenticationState {
    AUTHENTICATED,
    UNAUTHENTICATED,
    ERROR_AUTHENTICATION,
    ERROR_INPUT,
    LOADING
}