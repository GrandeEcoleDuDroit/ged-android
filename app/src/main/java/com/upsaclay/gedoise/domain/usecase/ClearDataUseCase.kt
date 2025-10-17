package com.upsaclay.gedoise.domain.usecase

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.news.domain.repository.AnnouncementRepository

class ClearDataUseCase(
    private val userRepository: UserRepository,
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val announcementRepository: AnnouncementRepository
) {
    suspend operator fun invoke() {
        userRepository.deleteLocalUser()
        conversationRepository.deleteLocalConversations()
        messageRepository.deleteLocalMessages()
        announcementRepository.deleteLocalAnnouncements()
    }
}