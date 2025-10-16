package com.upsaclay.gedoise.usecase

import com.upsaclay.common.domain.entity.BlockUserEvent
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.gedoise.domain.usecase.ListenBlockedUserEvents
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import com.upsaclay.message.domain.usecase.UpdateConversationDeleteTimeUseCase
import com.upsaclay.news.domain.repository.AnnouncementRepository
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
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase = mockk()
    private val updateConversationDeleteTimeUseCase: UpdateConversationDeleteTimeUseCase = mockk()
    private lateinit var useCase: ListenBlockedUserEvents
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        every { blockedUserRepository.blockUserEvent } returns emptyFlow()
        every { listenRemoteMessagesUseCase.stop(any()) } returns Unit
        coEvery { updateConversationDeleteTimeUseCase.execute(any(), any()) } returns Unit
        coEvery { announcementRepository.deleteLocalAnnouncement(any()) } returns Unit

        useCase = ListenBlockedUserEvents(
            blockedUserRepository = blockedUserRepository,
            announcementRepository = announcementRepository,
            listenRemoteMessagesUseCase = listenRemoteMessagesUseCase,
            updateConversationDeleteTimeUseCase = updateConversationDeleteTimeUseCase,
            scope = testScope
        )
    }

    @Test
    fun start_should_delete_local_announcement_of_blocked_user() {
        // Given
        val blockedUser = userFixture
        every { blockedUserRepository.blockUserEvent } returns flowOf(
            BlockUserEvent.Block(
                blockedUser.id
            )
        )

        // When
        useCase.start()

        // Then
        coVerify { announcementRepository.deleteLocalAnnouncements(blockedUser.id) }
    }
}