package com.upsaclay.authentication.domain

import com.upsaclay.core.data.local.SharedPreferenceFiles
import com.upsaclay.core.data.local.SharedPreferencesKeys
import com.upsaclay.core.domain.SharedPreferenceUseCase

class IsAuthenticatedUseCase(
    private val sharedPreferenceUseCase: SharedPreferenceUseCase
) {
    operator fun invoke(): Boolean = sharedPreferenceUseCase.getBoolean(
        SharedPreferenceFiles.AUTHENTICATION,
        SharedPreferencesKeys.IS_AUTHENTICATED,
        false
    )
}