package com.upsaclay.gedoise.viewmodel

import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.gedoise.presentation.profile.privacy.blockedusers.BlockedUsersViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BlockedUsersViewModelTest {
    private val blockedUserRepository: BlockedUserRepository = mockk()
    private val userRepository: UserRepository = mockk()

    private lateinit var viewModel: BlockedUsersViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        every { userRepository.currentUser } returns userFixture
        coEvery { blockedUserRepository.unblockUser(any(), any()) } returns Unit
        coEvery { blockedUserRepository.getLocalBlockedUserIds() } returns emptySet()
        coEvery { userRepository.getUser(any()) } returns userFixture

        viewModel = BlockedUsersViewModel(
            blockedUserRepository = blockedUserRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun unblockUser_should_unblock_user() {
        // Given
        val blockedUserId = "blockedUserId"

        // When
        viewModel.unblockUser(blockedUserId)

        // Then
        coVerify { blockedUserRepository.unblockUser(userFixture.id, blockedUserId) }
    }
}