package com.upsaclay.news.domain

import com.upsaclay.common.domain.entity.BlockUserEvent
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.ListenBlockUserEventsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import kotlin.test.Test

class ListenBlockUserEventsUseCaseTest {
    private val blockedUserRepository: BlockedUserRepository = mockk()
    private val announcementRepository: AnnouncementRepository = mockk()

    private lateinit var useCase: ListenBlockUserEventsUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        every { blockedUserRepository.blockUserEvent } returns emptyFlow()
        coEvery { announcementRepository.deleteLocalAnnouncement(any()) } returns Unit

        useCase = ListenBlockUserEventsUseCase(
            blockedUserRepository = blockedUserRepository,
            announcementRepository = announcementRepository,
            scope = testScope
        )
    }

    @Test
    fun start_should_delete_local_announcement_of_blocked_user() {
        // Given
        val blockedUser = userFixture
        every { blockedUserRepository.blockUserEvent } returns flowOf(BlockUserEvent.Block(blockedUser.id))

        // When
        useCase.start()

        // Then
        coVerify { announcementRepository.deleteLocalUserAnnouncements(blockedUser.id) }
    }
}