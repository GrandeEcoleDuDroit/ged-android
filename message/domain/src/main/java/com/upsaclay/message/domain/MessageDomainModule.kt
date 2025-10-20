package com.upsaclay.message.domain

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
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val messageDomainModule = module {
    singleOf(::DeleteConversationUseCase)
    singleOf(::GetConversationsUiUseCase)
    singleOf(::GetConversationUseCase)
    singleOf(::GetUnreadConversationsCountUseCase)
    singleOf(::ListenRemoteConversationsUseCase)
    singleOf(::ListenRemoteMessagesUseCase)
    singleOf(::NotificationMessageUseCase) { bind<NotificationUseCase<NotificationMessage>>() }
    singleOf(::SendMessageUseCase)
    singleOf(::UpdateConversationDeleteTimeUseCase)
}
