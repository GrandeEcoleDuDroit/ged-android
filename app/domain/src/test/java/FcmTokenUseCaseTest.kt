package com.upsaclay.gedoise.usecase

import com.upsaclay.app.domain.FcmTokenUseCase
import com.upsaclay.app.domain.fcmTokenFixture
import com.upsaclay.app.domain.repository.FcmTokenRepository
import com.upsaclay.authentication.domain.usecase.ListenAuthenticationStateUseCase
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FcmTokenUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val listenAuthenticationStateUseCase: ListenAuthenticationStateUseCase = mockk()
    private val fcmTokenRepository: FcmTokenRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var useCase: FcmTokenUseCase

    @Before
    fun setUp() {
        every { listenAuthenticationStateUseCase.authenticated } returns flowOf(true)
        every { connectivityObserver.connected } returns flowOf(true)
        coEvery { fcmTokenRepository.getUnsentFcmToken() } returns fcmTokenFixture
        coEvery { fcmTokenRepository.removeUnsentFcmToken() } returns Unit
        coEvery { fcmTokenRepository.storeUnsentFcmToken(any()) } returns Unit
        coEvery { fcmTokenRepository.sendFcmToken(any()) } returns Unit
        coEvery { fcmTokenRepository.generateToken() } returns fcmTokenFixture.value

        useCase = FcmTokenUseCase(
            userRepository = userRepository,
            fcmTokenRepository = fcmTokenRepository
        )
    }

    @Test
    fun sendUnsetToken_should_send_unsent_token() = runTest {
        // When
        useCase.sendUnsetToken()

        // Then
        coVerify { fcmTokenRepository.sendFcmToken(fcmTokenFixture) }
    }

    @Test
    fun generateNewToken_should_generate_new_token() = runTest {
        // Given
        coEvery { listenAuthenticationStateUseCase.authenticated } returns MutableStateFlow(false)

        // When
        useCase.generateNewToken()

        // Then
        coVerify { fcmTokenRepository.generateToken() }
    }

    @Test
    fun sendFcmToken_should_remove_unsent_token_when_success() = runTest {
        // Given

        // When
        useCase.sendFcmToken(fcmTokenFixture)

        // Then
        coVerify { fcmTokenRepository.removeUnsentFcmToken() }
    }

    @Test
    fun sendFcmToken_should_store_unsent_token_when_fails() = runTest {
        // Given
        coEvery { fcmTokenRepository.sendFcmToken(any()) } throws Exception()

        // When
        useCase.sendFcmToken(fcmTokenFixture)

        // Then
        coVerify { fcmTokenRepository.storeUnsentFcmToken(fcmTokenFixture) }
    }

    @Test
    fun storeToken_should_store_token() = runTest {
        // When
        useCase.storeToken(fcmTokenFixture)

        // Then
        coVerify { fcmTokenRepository.storeUnsentFcmToken(fcmTokenFixture) }
    }
}