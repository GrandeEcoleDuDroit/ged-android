package com.upsaclay.authentication.domain

import com.upsaclay.core.data.local.SharedPreferenceFiles
import com.upsaclay.core.data.local.SharedPreferencesKeys
import com.upsaclay.core.domain.SharedPreferenceUseCase

class LogoutUseCase(private val sharedPreferenceUseCase: SharedPreferenceUseCase) {

    operator fun invoke() {
        sharedPreferenceUseCase.storeBoolean(
            SharedPreferenceFiles.AUTHENTICATION,
            SharedPreferencesKeys.IS_AUTHENTICATED,
            false
        )
    }
}