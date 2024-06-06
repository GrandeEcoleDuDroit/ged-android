package com.upsaclay.authentication

import com.upsaclay.authentication.data.AuthenticationRepository
import com.upsaclay.authentication.data.AuthenticationState
import com.upsaclay.authentication.domain.GenerateHashUseCase
import com.upsaclay.authentication.domain.LoginParisSaclayUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {
    private lateinit var loginParisSaclayUseCase: LoginParisSaclayUseCase
    private lateinit var generateHashUseCase: GenerateHashUseCase
    private lateinit var authenticationRepository: AuthenticationRepository
    private val email = "firstname.name@universite-paris-saclay.fr"
    private val password = "strongpassword12345"
    private val hash = "9f64d054ac7498f0a16d46460ec8e4c2f0a1d2a02692652dd01aa3e25d9a72f5"

    @Before
    fun setUp() {
        generateHashUseCase = mockk()
        authenticationRepository = mockk()
        loginParisSaclayUseCase = LoginParisSaclayUseCase(
            authenticationRepository,
            generateHashUseCase,
        )

        coEvery {
            authenticationRepository.loginWithParisSaclay(any(), any(), any())
        } returns Result.success(AuthenticationState.AUTHENTICATED)
        coEvery { generateHashUseCase() } returns hash
    }

    @Test
    fun `login with paris saclay return authenticated when login is correct`() {
        runTest {
            val result = loginParisSaclayUseCase(email, password)
            assert(result.isSuccess)
        }
    }

    @Test
    fun `login with paris saclay return error authentication when login is incorrect`() {
        coEvery {
            authenticationRepository.loginWithParisSaclay(any(), any(), any())
        } returns Result.failure(Exception())
        runTest {
            val result = loginParisSaclayUseCase(email, password)
            assert(result.isFailure)
        }
    }
}