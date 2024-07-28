package com.upsaclay.authentication.domain.model

enum class RegistrationState {
    REGISTERED,
    NOT_REGISTERED,
    RECOGNIZED_ACCOUNT,
    UNRECOGNIZED_ACCOUNT,
    ERROR_REGISTRATION,
    ERROR_INPUT,
    LOADING
}