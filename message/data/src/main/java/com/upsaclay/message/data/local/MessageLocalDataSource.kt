package com.upsaclay.message.data.local

import com.upsaclay.message.data.local.dao.MessageDao
import com.upsaclay.message.data.local.model.LocalMessage
import kotlinx.coroutines.flow.Flow

internal class MessageLocalDataSource(private val messageDao: MessageDao) {
    fun getLastMessages(conversationId: String): Flow<List<LocalMessage>> = messageDao.getLastMessages(conversationId)

    suspend fun getMessages(conversationId: String, offset: Int): List<LocalMessage> = messageDao.getMessages(conversationId, offset)
}