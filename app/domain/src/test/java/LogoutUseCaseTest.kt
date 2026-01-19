package com.upsaclay.gedoise.usecase

import com.upsaclay.app.domain.usecase.LogoutUseCase
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.FcmTokenRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LogoutUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val fcmTokenRepository: FcmTokenRepository = mockk()

    private lateinit var useCase: LogoutUseCase

    @Before
    fun setUp() {
        every { userRepository.currentUser } returns userFixture
        coEvery { fcmTokenRepository.deleteToken(any()) } returns Unit
        coEvery { authenticationRepository.logout() } returns Unit

        useCase = LogoutUseCase(
            userRepository = userRepository,
            authenticationRepository = authenticationRepository,
            fcmTokenRepository = fcmTokenRepository
        )
    }

    @Test
    fun logoutUseCase_should_logout() = runTest {
        // When
        useCase()

        // Then
        coEvery { authenticationRepository.logout() }
    }

    @Test
    fun logoutUseCase_should_delete_token() = runTest {
        // When
        useCase()

        // Then
        coEvery { fcmTokenRepository.deleteToken(userFixture.id) }
    }
}