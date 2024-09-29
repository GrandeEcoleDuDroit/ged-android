package com.upsaclay.message.data.mapper

import com.upsaclay.message.data.model.MessageDTO
import com.upsaclay.message.data.remote.model.RemoteMessage
import java.time.LocalDateTime
import java.time.ZoneId

object MessageMapper {
    fun toDTO(remoteMessage: RemoteMessage) = MessageDTO(
        messageId = remoteMessage.messageId,
        senderId = remoteMessage.senderId,
        conversationId = remoteMessage.conversationId,
        text = remoteMessage.text,
        date = LocalDateTime.ofInstant(remoteMessage.timestamp.toInstant(), ZoneId.systemDefault()),
        isRead = remoteMessage.isRead,
        type = remoteMessage.type
    )
}