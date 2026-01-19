package com.upsaclay.message

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.notification.MessageNotificationManager
import com.upsaclay.message.notification.MessageNotificationPresenter
import com.upsaclay.message.presentation.chat.ChatViewModel
import com.upsaclay.message.presentation.conversation.ConversationViewModel
import com.upsaclay.message.presentation.conversation.createconversation.CreateConversationViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val BACKGROUND_SCOPE = named("BackgroundScope")

val messageModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
            SupervisorJob() +
                    Dispatchers.IO +
                    CoroutineExceptionHandler { _, throwable ->
                        System.err.print("Uncaught error in backgroundScope: $throwable")
                    }
        )
    }
    viewModelOf(::ConversationViewModel)
    viewModelOf(::CreateConversationViewModel)
    viewModel { (conversation: Conversation) ->
        ChatViewModel(
            conversation = conversation,
            userRepository = get(),
            conversationRepository = get(),
            messageRepository = get(),
            blockedUserRepository = get(),
            sendMessageUseCase = get(),
            messageNotificationManager = get(),
            deleteConversationUseCase = get(),
            generateIdUseCase = get()
        )
    }
    singleOf(::MessageNotificationPresenter)
    single {
        MessageNotificationManager(
            messageNotificationRepository = get(),
            messageNotificationPresenter = get(),
            navigationRequestUseCase = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
}