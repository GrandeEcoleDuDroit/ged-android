package com.upsaclay.authentication.domain

import android.content.Context
import com.upsaclay.authentication.data.AuthenticationState
import com.upsaclay.core.data.SharedPreferenceFiles
import com.upsaclay.core.data.SharedPreferencesKeys

class IsAuthenticatedUseCase(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(
        SharedPreferenceFiles.AUTHENTICATION, Context.MODE_PRIVATE
    )

    operator fun invoke(): AuthenticationState {
        val isAuthenticated = sharedPreferences.getBoolean(
            SharedPreferencesKeys.AUTHENTICATED, false
        )
        return if (isAuthenticated) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

}