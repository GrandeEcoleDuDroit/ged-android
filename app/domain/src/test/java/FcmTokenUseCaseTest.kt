package com.upsaclay.gedoise.usecase

import com.upsaclay.app.domain.fcmTokenFixture
import com.upsaclay.app.domain.usecase.FcmTokenUseCase
import com.upsaclay.common.domain.entity.FcmToken
import com.upsaclay.common.domain.repository.FcmTokenRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FcmTokenUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val fcmTokenRepository: FcmTokenRepository = mockk()

    private lateinit var useCase: FcmTokenUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        every { userRepository.currentUser } returns userFixture
        coEvery { fcmTokenRepository.getFcmToken() } returns fcmTokenFixture
        coEvery { fcmTokenRepository.storeFcmToken(any()) } returns Unit
        coEvery { fcmTokenRepository.sendFcmToken(any(), any()) } returns Unit
        coEvery { fcmTokenRepository.generateToken() } returns fcmTokenFixture.token

        useCase = FcmTokenUseCase(
            userRepository = userRepository,
            fcmTokenRepository = fcmTokenRepository,
            scope = testScope
        )
    }

    @Test
    fun sendUnsetToken_should_send_unsent_token() = runTest {
        // Given
        val fcmToken = fcmTokenFixture.copy(sent = false)
        coEvery { fcmTokenRepository.getFcmToken() } returns fcmTokenFixture.copy(sent = false)

        // When
        useCase.sendUnsentToken()

        // Then
        coVerify { fcmTokenRepository.sendFcmToken(userFixture.id, fcmToken.token) }
    }

    @Test
    fun sendUnsetToken_should_send_new_token_when_not_found() = runTest {
        // Given
        val newToken = "newToken"
        coEvery { fcmTokenRepository.getFcmToken() } returns null
        coEvery { fcmTokenRepository.generateToken() } returns newToken

        // When
        useCase.sendUnsentToken()

        // Then
        coVerify { fcmTokenRepository.sendFcmToken(userFixture.id, newToken) }
    }

    @Test
    fun sendUnsetToken_should_store_token_as_sent_when_sending_succeeds() = runTest {
        // Given
        val fcmToken = FcmToken("token", false)
        coEvery { fcmTokenRepository.getFcmToken() } returns fcmToken

        // When
        useCase.sendUnsentToken()

        // Then
        coVerify { fcmTokenRepository.storeFcmToken(fcmToken.copy(sent = true)) }
    }

    @Test
    fun sendUnsetToken_should_store_token_as_unsent_when_sending_fails() = runTest {
        // Given
        val fcmToken = FcmToken("token", false)
        coEvery { fcmTokenRepository.getFcmToken() } returns fcmToken
        coEvery { fcmTokenRepository.sendFcmToken(any(), any()) } throws Exception()

        // When
        useCase.sendUnsentToken()

        // Then
        coVerify { fcmTokenRepository.storeFcmToken(fcmToken) }
    }

    @Test
    fun onNewTokenReceived_should_do_nothing_when_token_is_the_same() = runTest {
        // Given
        val token = "token"
        coEvery { fcmTokenRepository.getFcmToken() } returns fcmTokenFixture.copy(token = token)

        // When
        useCase.onNewTokenReceived(token)

        // Then
        coVerify(exactly = 0) { fcmTokenRepository.storeFcmToken(any()) }
        coVerify(exactly = 0) { fcmTokenRepository.sendFcmToken(any(), any()) }
    }

    @Test
    fun onNewTokenReceived_should_store_new_token() = runTest {
        // Given
        val token = "token"
        coEvery { fcmTokenRepository.getFcmToken() } returns fcmTokenFixture.copy(token = "otherToken")

        // When
        useCase.onNewTokenReceived(token)

        // Then
        coVerify { fcmTokenRepository.storeFcmToken(FcmToken(token, false)) }
    }

    @Test
    fun onNewTokenReceived_should_send_new_token() = runTest {
        // Given
        val token = "token"
        coEvery { fcmTokenRepository.getFcmToken() } returns fcmTokenFixture.copy(token = "otherToken")

        // When
        useCase.onNewTokenReceived(token)

        // Then
        coVerify { fcmTokenRepository.sendFcmToken(userFixture.id, token) }
    }
}