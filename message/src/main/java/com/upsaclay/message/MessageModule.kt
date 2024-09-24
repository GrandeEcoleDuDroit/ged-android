package com.upsaclay.message

import com.upsaclay.message.data.remote.api.ConversationApi
import com.upsaclay.message.data.remote.api.ConversationApiImpl
import com.upsaclay.message.data.repository.ConversationRepositoryImpl
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.usecase.GetAllConversationsUseCase
import com.upsaclay.message.domain.usecase.GetConversationUseCase
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import com.upsaclay.message.presentation.viewmodel.ChatViewModel
import com.upsaclay.message.presentation.viewmodel.ConversationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val messageModule = module {
    viewModelOf(::ConversationViewModel)
    viewModel { (conversationId: String?, interlocutorId: Int?) ->
        ChatViewModel(
            conversationId = conversationId,
            interlocutorId = interlocutorId,
            getConversationUseCase = get(),
            getCurrentUserUseCase = get(),
            getUserUseCase = get(),
            sendMessageUseCase = get()
        )
    }

    singleOf(::ConversationRepositoryImpl) { bind<ConversationRepository>()  }
    singleOf(::ConversationApiImpl) { bind<ConversationApi>() }

    singleOf(::GetAllConversationsUseCase)
    singleOf(::GetConversationUseCase)
    singleOf(::SendMessageUseCase)
}