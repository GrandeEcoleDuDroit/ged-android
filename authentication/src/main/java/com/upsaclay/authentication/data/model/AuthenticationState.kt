package com.upsaclay.authentication.data.model

enum class AuthenticationState {
    AUTHENTICATED,
    UNAUTHENTICATED,
    ERROR_AUTHENTICATION,
    ERROR_INPUT,
    LOADING
}