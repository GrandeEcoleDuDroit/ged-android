package com.upsaclay.authentication.domain.model

enum class AuthenticationState {
    AUTHENTICATED,
    UNAUTHENTICATED,
    AUTHENTICATION_ERROR,
    INPUT_ERROR,
    LOADING
}