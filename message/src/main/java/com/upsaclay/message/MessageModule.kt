package com.upsaclay.message

import com.upsaclay.message.domain.usecase.GetAllConversationsUseCase
import com.upsaclay.message.domain.usecase.GetConversationUseCase
import com.upsaclay.message.presentation.viewmodel.ConversationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val messageModule = module {
    viewModelOf(::ConversationViewModel)

    singleOf(::GetAllConversationsUseCase)
    singleOf(::GetConversationUseCase)
}