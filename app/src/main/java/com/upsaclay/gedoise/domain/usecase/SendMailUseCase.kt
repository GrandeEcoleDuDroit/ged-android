package com.upsaclay.gedoise.domain.usecase

import com.upsaclay.common.domain.repository.UserRepository

class SendMailUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(){

    }

}
