package com.upsaclay.gedoise.usecase

import com.upsaclay.app.domain.ListenRemoteUserUseCase
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ListenRemoteUserUseCaseTest {
    private val userRepository: UserRepository = mockk()

    private lateinit var useCase: ListenRemoteUserUseCase

    @Before
    fun setup() {
        every { userRepository.user } returns flowOf(userFixture)
        every { userRepository.getUserFlow(any()) } returns flowOf(userFixture2)
        coEvery { userRepository.storeUser(any()) } returns Unit

        useCase = ListenRemoteUserUseCase(
            userRepository = userRepository
        )
    }

    @Test
    fun start_should_update_local_user_when_different_from_remote() = runTest {
        // When
        useCase.start()

        // Then
        coVerify { userRepository.storeUser(userFixture2) }
    }
}