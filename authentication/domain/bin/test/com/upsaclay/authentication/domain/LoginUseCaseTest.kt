package com.upsaclay.authentication.domain

import com.upsaclay.authentication.domain.entity.AuthenticationState
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.authentication.domain.usecase.LoginUseCase
import io.mockk.awaits
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {
    private val authenticationRepository: AuthenticationRepository = mockk()

    private lateinit var useCase: LoginUseCase
    private val userId = "userId1234"
    private val email = "email@example.com"
    private val password = "password123"

    @Before
    fun setUp() {
        coEvery { authenticationRepository.loginWithEmailAndPassword(any(), any()) } returns userId
        coEvery { authenticationRepository.storeAuthenticationState(any()) } returns Unit

        useCase = LoginUseCase(
            authenticationRepository = authenticationRepository
        )
    }

    @Test
    fun login_should_set_authentication_state_to_authenticated_when_authentication_succeeds() = runTest {
        // When
        useCase.execute(email, password)

        // Then
        coVerify {
            authenticationRepository.storeAuthenticationState(AuthenticationState.Authenticated(userId))
        }
    }

    @Test(expected = TimeoutCancellationException::class)
    fun login_should_throw_TimeoutCancellationException_when_takes_more_10_seconds() = runTest {
        // Given
        coEvery { authenticationRepository.loginWithEmailAndPassword(email, password) } just awaits

        // When
        useCase.execute(email, password)
    }
}