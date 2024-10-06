package com.upsaclay.authentication.domain.model

enum class RegistrationState {
    REGISTERED,
    NOT_REGISTERED,
    RECOGNIZED_ACCOUNT,
    UNRECOGNIZED_ACCOUNT,
    REGISTRATION_ERROR,
    INPUT_ERROR,
    LOADING
}