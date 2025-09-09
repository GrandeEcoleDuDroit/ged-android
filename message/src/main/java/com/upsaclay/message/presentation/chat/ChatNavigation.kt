package com.upsaclay.message.presentation.chat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.message.domain.converter.ConversationJsonConverter
import com.upsaclay.message.domain.entity.Conversation
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoute(val conversationJson: String): Route {
    companion object {
        const val NAME = "ChatRoute"
        const val CONVERSATION_JSON_ARGUMENT = "conversationJson"
    }
}


fun NavController.navigateToChat(
    conversation: Conversation,
    navOptionsBuilder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(
        route = ChatRoute(ConversationJsonConverter.toConversationJson(conversation)),
        builder = navOptionsBuilder
    )
}

fun NavController.navigateToChat(
    conversationMessageJson: String,
    navOptionsBuilder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = ChatRoute(conversationMessageJson), builder = navOptionsBuilder)
}

fun NavGraphBuilder.chatScreen(onBackClick: () -> Unit) {
    composable<ChatRoute> { entry ->
        val conversation = entry.toRoute<ChatRoute>().conversationJson
            .let { ConversationJsonConverter.toConversation(it) } ?: return@composable onBackClick()

        ChatDestination(
            conversation = conversation,
            onBackClick = onBackClick
        )
    }
}