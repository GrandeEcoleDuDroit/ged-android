package com.upsaclay.gedoise.usecase

import com.upsaclay.app.domain.usecase.ListenBlockedUserEventsUseCase
import com.upsaclay.common.domain.entity.BlockUserEvent
import com.upsaclay.common.domain.entity.BlockedUser
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.announcement.AnnouncementRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import java.time.LocalDateTime
import kotlin.test.Test

class ListenBlockUserEventsUseCaseTest {
    private val blockedUserRepository: BlockedUserRepository = mockk()
    private val announcementRepository: AnnouncementRepository = mockk()
    private lateinit var useCase: ListenBlockedUserEventsUseCase

    @Before
    fun setUp() {
        every { blockedUserRepository.blockUserEvent } returns emptyFlow()
        coEvery { announcementRepository.deleteLocalUserAnnouncements(any()) } returns Unit

        useCase = ListenBlockedUserEventsUseCase(
            blockedUserRepository = blockedUserRepository,
            announcementRepository = announcementRepository
        )
    }

    @Test
    fun start_should_delete_local_announcement_of_blocked_user() = runTest {
        // Given
        val blockedUser = userFixture
        every { blockedUserRepository.blockUserEvent } returns flowOf(BlockUserEvent.Block(BlockedUser(userFixture.id, LocalDateTime.now())))

        // When
        useCase.start()

        // Then
        coVerify { announcementRepository.deleteLocalUserAnnouncements(blockedUser.id) }
    }
}