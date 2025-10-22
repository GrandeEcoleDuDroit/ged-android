package com.upsaclay.message.data

import com.upsaclay.common.data.GED_SERVER_QUALIFIER
import com.upsaclay.common.data.e
import com.upsaclay.message.data.local.ConversationLocalDataSource
import com.upsaclay.message.data.local.ConversationMessageLocalDataSource
import com.upsaclay.message.data.local.MessageLocalDataSource
import com.upsaclay.message.data.local.NotificationMessageLocalDataSource
import com.upsaclay.message.data.remote.ConversationRemoteDataSource
import com.upsaclay.message.data.remote.MessageRemoteDataSource
import com.upsaclay.message.data.remote.NotificationMessageRemoteDataSource
import com.upsaclay.message.data.remote.api.ConversationApi
import com.upsaclay.message.data.remote.api.ConversationApiImpl
import com.upsaclay.message.data.remote.api.MessageApi
import com.upsaclay.message.data.remote.api.MessageApiImpl
import com.upsaclay.message.data.remote.api.MessageServerApi
import com.upsaclay.message.data.remote.api.NotificationMessageApi
import com.upsaclay.message.data.remote.api.NotificationMessageApiImpl
import com.upsaclay.message.data.repository.ConversationMessageRepositoryImpl
import com.upsaclay.message.data.repository.ConversationRepositoryImpl
import com.upsaclay.message.data.repository.MessageRepositoryImpl
import com.upsaclay.message.data.repository.NotificationMessageRepositoryImpl
import com.upsaclay.message.data.worker.StartupMessageWorker
import com.upsaclay.message.domain.repository.ConversationMessageRepository
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.repository.NotificationMessageRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

private val BACKGROUND_SCOPE = named("BackgroundScope")

val messageDataModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
            SupervisorJob() +
                    Dispatchers.IO +
                    CoroutineExceptionHandler { _, throwable ->
                        e("Uncaught error in backgroundScope", throwable)
                    }
        )
    }

    singleOf(::ConversationApiImpl) { bind<ConversationApi>() }
    singleOf(::ConversationRemoteDataSource)
    singleOf(::ConversationLocalDataSource)
    singleOf(::ConversationMessageLocalDataSource)
    singleOf(::ConversationRepositoryImpl) { bind<ConversationRepository>() }
    single<ConversationMessageRepository> {
        ConversationMessageRepositoryImpl(
            conversationMessageLocalDataSource = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }

    singleOf(::MessageApiImpl) { bind<MessageApi>() }
    single {
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(MessageServerApi::class.java)
    }
    singleOf(::MessageRemoteDataSource)
    singleOf(::MessageLocalDataSource)
    singleOf(::MessageRepositoryImpl) { bind<MessageRepository>() }
    single {
        StartupMessageWorker(context = androidContext())
    }

    singleOf(::NotificationMessageApiImpl) { bind<NotificationMessageApi>() }
    singleOf(::NotificationMessageRemoteDataSource)
    singleOf(::NotificationMessageLocalDataSource)
    singleOf(::NotificationMessageRepositoryImpl) { bind<NotificationMessageRepository>() }
}