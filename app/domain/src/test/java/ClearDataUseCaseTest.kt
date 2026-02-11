package com.upsaclay.gedoise.usecase

import com.upsaclay.app.domain.usecase.ClearDataUseCase
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.news.domain.announcement.AnnouncementRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClearDataUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val conversationRepository: ConversationRepository = mockk()
    private val messageRepository: MessageRepository = mockk()
    private val announcementRepository: AnnouncementRepository = mockk()
    private val blockedUserRepository: BlockedUserRepository = mockk()

    private lateinit var useCase: ClearDataUseCase

    @Before
    fun setUp() {
        coEvery { userRepository.deleteLocalUser() } returns Unit
        coEvery { conversationRepository.deleteLocalConversations() } returns Unit
        coEvery { messageRepository.deleteLocalMessages() } returns Unit
        coEvery { announcementRepository.deleteLocalAnnouncements() } returns Unit
        coEvery { blockedUserRepository.deleteLocalBlockedUsers() } returns Unit

        useCase = ClearDataUseCase(
            userRepository = userRepository,
            conversationRepository = conversationRepository,
            messageRepository = messageRepository,
            announcementRepository = announcementRepository,
            blockedUserRepository = blockedUserRepository
        )
    }

    @Test
    fun clearDataUseCase_should_delete_all_local_data() = runTest {
        // When
        useCase.execute()

        // Then
        coVerify { userRepository.deleteLocalUser() }
        coVerify { conversationRepository.deleteLocalConversations() }
        coVerify { messageRepository.deleteLocalMessages() }
        coVerify { announcementRepository.deleteLocalAnnouncements() }
        coVerify { blockedUserRepository.deleteLocalBlockedUsers() }
    }
}