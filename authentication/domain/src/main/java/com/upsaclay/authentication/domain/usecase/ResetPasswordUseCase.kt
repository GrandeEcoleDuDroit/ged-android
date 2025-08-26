package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException

class ResetPasswordUseCase(
    private var authenticationRepository: AuthenticationRepository,
    private var connectivityObserver: ConnectivityObserver
) {
    suspend operator fun invoke(email : String){
        if(!connectivityObserver.isConnected)
            throw NoInternetConnectionException()
        authenticationRepository.createANewPassword(email)
    }
}