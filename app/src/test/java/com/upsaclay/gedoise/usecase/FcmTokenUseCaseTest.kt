package com.upsaclay.gedoise.usecase

import com.upsaclay.authentication.domain.usecase.ListenAuthenticationStateUseCase
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.fcmTokenFixture
import com.upsaclay.common.domain.repository.FcmTokenRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.gedoise.domain.usecase.FcmTokenUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FcmTokenUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val listenAuthenticationStateUseCase: ListenAuthenticationStateUseCase = mockk()
    private val fcmTokenRepository: FcmTokenRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var useCase: FcmTokenUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        every { listenAuthenticationStateUseCase.authenticated } returns flowOf(true)
        every { connectivityObserver.connected } returns flowOf(true)
        coEvery { fcmTokenRepository.getUnsentFcmToken() } returns fcmTokenFixture
        coEvery { fcmTokenRepository.removeUnsentFcmToken() } returns Unit
        coEvery { fcmTokenRepository.storeUnsentFcmToken(any()) } returns Unit
        coEvery { fcmTokenRepository.sendFcmToken(any()) } returns Unit

        useCase = FcmTokenUseCase(
            userRepository = userRepository,
            listenAuthenticationStateUseCase = listenAuthenticationStateUseCase,
            fcmTokenRepository = fcmTokenRepository,
            connectivityObserver = connectivityObserver,
            scope = testScope
        )
    }

    @Test
    fun fcmTokenUseCase_should_send_unsent_token_when_authenticated() {
        // When
        useCase.listenEvents()

        // Then
        coVerify { fcmTokenRepository.sendFcmToken(fcmTokenFixture) }
    }

    @Test
    fun fcmTokenUseCase_should_delete_token_when_unauthenticated() {
        // Given
        coEvery { listenAuthenticationStateUseCase.authenticated } returns MutableStateFlow(false)

        // When
        useCase.listenEvents()

        // Then
        coVerify { fcmTokenRepository.removeUnsentFcmToken() }
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