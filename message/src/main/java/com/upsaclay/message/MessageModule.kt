package com.upsaclay.message

import com.upsaclay.message.data.repository.ConversationRepositoryImpl
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.usecase.GetAllConversationsUseCase
import com.upsaclay.message.presentation.viewmodel.ConversationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val messageModule = module {
    viewModelOf(::ConversationViewModel)

    singleOf(::ConversationRepositoryImpl) { bind<ConversationRepository>()  }

    singleOf(::GetAllConversationsUseCase)
}