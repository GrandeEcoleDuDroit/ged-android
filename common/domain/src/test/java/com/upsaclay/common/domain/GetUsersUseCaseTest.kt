package com.upsaclay.common.domain

import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.GetUsersUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetUsersUseCaseTest {
    private val userRepository: UserRepository = mockk()

    private lateinit var useCase: GetUsersUseCase

    @Before
    fun setUp() {
        coEvery { userRepository.getUsers() } returns listOf(userFixture)

        useCase = GetUsersUseCase(userRepository= userRepository)
    }

    @Test
    fun deleted_users_should_not_be_fetched() = runTest {
        // Given
        val users = listOf(
            userFixture,
            userFixture.copy(state = User.UserState.DELETED)
        )
        coEvery { userRepository.getUsers() } returns users

        // When
        val result = useCase()

        // Then
        assert(result == listOf(userFixture))
    }
}