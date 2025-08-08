package com.upsaclay.gedoise.domain.usecase

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.gedoise.domain.repository.SendMailRepository

class SendMailUseCase(
    private val userRepository: UserRepository,
    private val sendMailRepository : SendMailRepository
) {
    operator fun invoke(){

    }

}
