package com.upsaclay.message.presentation.conversation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.domain.entity.ConversationUi
import com.upsaclay.message.domain.fixtures.conversationsUiFixture

class ConversationPreviewParameterProvider: PreviewParameterProvider<List<ConversationUi>> {
    override val values = sequenceOf(conversationsUiFixture)
}

class CreateConversationPreviewParameterProvider: PreviewParameterProvider<List<ConversationUi>> {
    override val values = sequenceOf(conversationsUiFixture)
}

data class CreateConversationPreviewParameterData(
    val users: List<User>
)