package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository

class ForgottenPasswordUseCase(private val authenticationRepository : AuthenticationRepository) {
    suspend fun execute(email : String){
        authenticationRepository.forgotPassword(email)
    }

}