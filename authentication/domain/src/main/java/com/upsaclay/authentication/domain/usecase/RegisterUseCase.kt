package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.entity.exception.AuthenticationException
import com.upsaclay.authentication.domain.entity.exception.AuthenticationException.AuthExceptionType.USER_NOT_WHITE_LISTED_EXCEPTION
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.repository.WhiteListRepository

class RegisterUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository,
    private val whiteListRepository: WhiteListRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        schoolLevel: SchoolLevel
    ) {
        if (!whiteListRepository.isUserWhiteListed(email)) {
            throw AuthenticationException(USER_NOT_WHITE_LISTED_EXCEPTION, Exception())
        }

        val userId = authenticationRepository.registerWithEmailAndPassword(email, password)
        val user = User(
            id = userId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            schoolLevel = schoolLevel,
            admin = false,
            profilePictureUrl = null,
            tester = false
        )
        userRepository.createUser(user)
        authenticationRepository.setAuthenticated(true)
    }
}