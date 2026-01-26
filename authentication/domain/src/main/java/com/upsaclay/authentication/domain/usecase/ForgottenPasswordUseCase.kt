package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository

class ForgottenPasswordUseCase(private val authenticationRepository : AuthenticationRepository) {
    fun execute(email : String){
        try {
            TODO("not implemented yet")
        } catch (e : Exception){

        }
    }

}