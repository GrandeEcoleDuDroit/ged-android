package com.upsaclay.message.domain

import com.upsaclay.common.domain.NotificationApi
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.usecase.NotificationMessageUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class NotificationMessageUseCaseTest {
    private val notificationApi: NotificationApi = mockk()
    private val userRepository: UserRepository = mockk()

    private lateinit var useCase: NotificationMessageUseCase

    @Before
    fun setUp() {
        every { userRepository.currentUser } returns userFixture
        coEvery { notificationApi.sendNotification<Any>(any(), any()) } returns Unit

        useCase = NotificationMessageUseCase(
            notificationApi = notificationApi,
            userRepository = userRepository,
        )
    }

    @Test
    fun sendNotification_should_send_notification() = runTest {
        // When
        useCase.sendNotification(notificationMessageFixture)

        // Then
        coVerify { notificationApi.sendNotification<Any>(any(), any()) }
    }
}