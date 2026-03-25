package com.upsaclay.message.presentation.chat

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.fixtures.conversationFixture
import com.upsaclay.message.domain.fixtures.messagesFixture

class ChatPreviewParameterProvider: PreviewParameterProvider<ChatPreviewParameterData> {
    override val values = sequenceOf(ChatPreviewParameterData(conversationFixture, messagesFixture))
}

data class ChatPreviewParameterData(
    val conversation: Conversation,
    val messages: List<Message>
)