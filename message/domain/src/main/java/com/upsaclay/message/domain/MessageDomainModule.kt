package com.upsaclay.message.domain

import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
import com.upsaclay.message.domain.usecase.GetConversationUseCase
import com.upsaclay.message.domain.usecase.GetConversationsUiUseCase
import com.upsaclay.message.domain.usecase.GetUnreadConversationsCountUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import com.upsaclay.message.domain.usecase.UpdateConversationDeleteTimeUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val BACKGROUND_SCOPE = named("BackgroundScope")

val messageDomainModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
            SupervisorJob() +
                    Dispatchers.IO +
                    CoroutineExceptionHandler { _, throwable ->
                        System.err.print("Uncaught error in backgroundScope: $throwable")
                    }
        )
    }

    singleOf(::DeleteConversationUseCase)
    singleOf(::GetConversationsUiUseCase)
    singleOf(::GetConversationUseCase)
    singleOf(::GetUnreadConversationsCountUseCase)
    singleOf(::ListenRemoteConversationsUseCase)
    singleOf(::ListenRemoteMessagesUseCase)
    single {
        SendMessageUseCase(
            conversationRepository = get(),
            messageRepository = get(),
            messageNotificationRepository = get(),
            userRepository = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::UpdateConversationDeleteTimeUseCase)
}
