package com.upsaclay.app.domain.usecase

import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.news.domain.announcement.AnnouncementRepository

class ClearDataUseCase(
    private val userRepository: UserRepository,
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val announcementRepository: AnnouncementRepository,
    private val blockedUserRepository: BlockedUserRepository,
) {
    suspend fun execute() {
        userRepository.deleteLocalUser()
        conversationRepository.deleteLocalConversations()
        messageRepository.deleteLocalMessages()
        announcementRepository.deleteLocalAnnouncements()
        blockedUserRepository.deleteLocalBlockedUsers()
    }
}