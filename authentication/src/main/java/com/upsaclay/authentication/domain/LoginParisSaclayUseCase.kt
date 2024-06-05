package com.upsaclay.authentication.domain

import android.content.Context
import android.util.Log
import com.upsaclay.authentication.data.AuthenticationRepository
import com.upsaclay.authentication.data.AuthenticationState
import com.upsaclay.core.data.SharedPreferenceFiles
import com.upsaclay.core.data.SharedPreferencesKeys

class LoginParisSaclayUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val generateHashUseCase: GenerateHashUseCase,
    context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(
        SharedPreferenceFiles.AUTHENTICATION, Context.MODE_PRIVATE
    )

    suspend operator fun invoke(email: String, password: String): AuthenticationState {
        val hash = generateHashUseCase()
        val result = authenticationRepository.loginWithParisSaclay(email, password, hash)
        return if (result.isSuccess) {
            sharedPreferences.edit()
                .putBoolean(SharedPreferencesKeys.AUTHENTICATED, true)
                .apply()
            AuthenticationState.AUTHENTICATED
        } else {
            Log.e(
                "LoginParisSaclayUseCase",
                "Error while trying to login : ${result.exceptionOrNull()}"
            )
            AuthenticationState.ERROR_AUTHENTICATION
        }
    }
}