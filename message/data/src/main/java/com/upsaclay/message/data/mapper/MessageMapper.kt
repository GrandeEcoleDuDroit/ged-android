package com.upsaclay.message.data.mapper

import com.upsaclay.message.data.model.MessageDTO
import com.upsaclay.message.data.remote.model.RemoteMessage
import com.upsaclay.message.domain.model.Message
import java.time.LocalDateTime
import java.time.ZoneId

internal object MessageMapper {
    fun toDTO(remoteMessage: RemoteMessage) = MessageDTO(
        messageId = remoteMessage.messageId,
        senderId = remoteMessage.senderId,
        conversationId = remoteMessage.conversationId,
        text = remoteMessage.text,
        date = LocalDateTime.ofInstant(remoteMessage.timestamp.toInstant(), ZoneId.systemDefault()),
        isRead = remoteMessage.isRead,
        type = remoteMessage.type
    )

    fun toDomain(messageDTO: MessageDTO) = Message(
        id = messageDTO.messageId,
        senderId = messageDTO.senderId,
        text = messageDTO.text,
        date = messageDTO.date,
        isRead = messageDTO.isRead,
        type = messageDTO.type
    )
}