package com.upsaclay.message.domain

import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.usecase.NotificationUseCase
import com.upsaclay.message.domain.entity.NotificationMessage
import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
import com.upsaclay.message.domain.usecase.GetConversationUseCase
import com.upsaclay.message.domain.usecase.GetConversationsUiUseCase
import com.upsaclay.message.domain.usecase.GetUnreadConversationsCountUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import com.upsaclay.message.domain.usecase.NotificationMessageUseCase
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import com.upsaclay.message.domain.usecase.UpdateConversationDeleteTimeUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.bind
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
                e("Uncaught error in backgroundScope", throwable)
            }
        )
    }

    single {
        DeleteConversationUseCase(
            conversationRepository = get(),
            messageRepository = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::GetConversationsUiUseCase)
    singleOf(::GetConversationUseCase)
    singleOf(::GetUnreadConversationsCountUseCase)
    single {
        ListenRemoteConversationsUseCase(
            userRepository = get(),
            conversationRepository = get(),
            listenRemoteMessagesUseCase = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    single {
        ListenRemoteMessagesUseCase(
            messageRepository = get(),
            blockedUserRepository = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::NotificationMessageUseCase) { bind<NotificationUseCase<NotificationMessage>>() }
    single {
        SendMessageUseCase(
            conversationRepository = get(),
            messageRepository = get(),
            notificationMessageUseCase = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::UpdateConversationDeleteTimeUseCase)
}
