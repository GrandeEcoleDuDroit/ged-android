package com.upsaclay.authentication.domain

import com.upsaclay.authentication.domain.entity.AuthenticationException
import com.upsaclay.authentication.domain.entity.AuthenticationState
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.authentication.domain.usecase.RegisterUseCase
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.repository.WhiteListRepository
import com.upsaclay.common.domain.userFixture
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RegisterUseCaseTest {
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val whiteListRepository: WhiteListRepository = mockk()

    private lateinit var useCase: RegisterUseCase
    private val email = userFixture.email
    private val password = "password123"
    private val firstName = userFixture.firstName
    private val lastName = userFixture.lastName
    private val schoolLevel = userFixture.schoolLevel

    @Before
    fun setUp() {
        coEvery { whiteListRepository.isUserWhiteListed(any()) } returns true
        coEvery { authenticationRepository.registerWithEmailAndPassword(any(), any()) } returns userFixture.id
        coEvery { userRepository.createUser(any()) } returns Unit
        coEvery { authenticationRepository.storeAuthenticationState(any()) } returns Unit

        useCase = RegisterUseCase(
            authenticationRepository = authenticationRepository,
            userRepository = userRepository,
            whiteListRepository = whiteListRepository
        )
    }

    @Test
    fun register_should_register_user() = runTest {
        // When
        useCase.execute(email, password, firstName, lastName, schoolLevel)

        // Then
        coVerify { authenticationRepository.registerWithEmailAndPassword(email, password) }
    }

    @Test
    fun register_should_create_user_when_registered() = runTest {
        // When
        useCase.execute(email, password, firstName, lastName, schoolLevel)

        // Then
        coVerify { userRepository.createUser(any()) }
    }

    @Test
    fun register_should_set_authentication_state_to_authenticated_when_registered() = runTest {
        // Given
        val userId = "userId1234"
        coEvery { authenticationRepository.registerWithEmailAndPassword(any(), any()) } returns userId

        // When
        useCase.execute(email, password, firstName, lastName, schoolLevel)

        // Then
        coVerify { authenticationRepository.storeAuthenticationState(AuthenticationState.Authenticated(userId)) }
    }

    @Test(expected = AuthenticationException::class)
    fun register_should_throw_authentication_exception_when_user_not_white_listed() = runTest {
        // Given
        coEvery { whiteListRepository.isUserWhiteListed(any()) } returns false

        // When
        useCase.execute(email, password, firstName, lastName, schoolLevel)
    }
}