package com.upsaclay.message.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import org.koin.java.KoinJavaComponent.inject

internal class SendUnsentConversationWorker (
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {
    private val conversationRepository: ConversationRepository by inject(ConversationRepository::class.java)
    private val messageRepository: MessageRepository by inject(MessageRepository::class.java)
    private val userRepository: UserRepository by inject(UserRepository::class.java)

    override suspend fun doWork(): Result {
        return try {
            val userId = userRepository.getCurrentUser()?.id ?: return Result.failure()
            conversationRepository.getConversations().forEach { conversation ->
                when (conversation.state) {
                    ConversationState.CREATING -> {
                        conversationRepository.createRemoteConversation(conversation, userId)
                    }

                    ConversationState.DELETING -> {
                        conversation.deleteTime?.let {
                            conversationRepository.deleteConversation(conversation, userId, it)
                            messageRepository.deleteLocalMessages(conversation.id)
                        }
                    }

                    else -> Unit
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}