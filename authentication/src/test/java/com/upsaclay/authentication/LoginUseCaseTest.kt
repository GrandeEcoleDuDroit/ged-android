package com.upsaclay.authentication

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.authentication.domain.usecase.GenerateHashUseCase
import com.upsaclay.authentication.domain.usecase.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var generateHashUseCase: GenerateHashUseCase
    private lateinit var authenticationRepository: AuthenticationRepository
    private val email = "firstname.name@universite-paris-saclay.fr"
    private val password = "strongpassword12345"
    private val hash = "9f64d054ac7498f0a16d46460ec8e4c2f0a1d2a02692652dd01aa3e25d9a72f5"

    @Before
    fun setUp() {
        generateHashUseCase = mockk()
        authenticationRepository = mockk()
        loginUseCase = LoginUseCase(
            authenticationRepository,
            generateHashUseCase
        )

        coEvery {
            authenticationRepository.loginWithParisSaclay(any(), any(), any())
        } returns Result.success("")
        coEvery { generateHashUseCase() } returns hash
    }

    @Test
    fun login_with_paris_saclay_return_success_when_login_is_correct() {
        runTest {
            val result = loginUseCase(email, password)
            assert(result.isSuccess)
        }
    }

    @Test
    fun login_with_paris_saclay_return_fail_when_login_is_incorrect() {
        coEvery {
            authenticationRepository.loginWithParisSaclay(any(), any(), any())
        } returns Result.failure(Exception())
        runTest {
            val result = loginUseCase(email, password)
            assert(result.isFailure)
        }
    }
}