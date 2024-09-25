package com.upsaclay.message.data.repository

import com.upsaclay.message.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    val messages: Flow<List<Message>>
}