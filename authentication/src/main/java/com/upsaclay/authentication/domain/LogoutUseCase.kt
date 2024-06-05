package com.upsaclay.authentication.domain

import android.content.Context
import com.upsaclay.core.data.SharedPreferenceFiles

class LogoutUseCase(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        SharedPreferenceFiles.AUTHENTICATION, Context.MODE_PRIVATE
    )

    operator fun invoke() {
        sharedPreferences.edit()
            .putBoolean(SharedPreferenceFiles.AUTHENTICATION, false)
            .apply()
    }
}