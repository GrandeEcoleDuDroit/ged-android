package com.upsaclay.app.domain

import com.upsaclay.app.domain.usecase.ClearDataUseCase
import com.upsaclay.app.domain.usecase.DeleteAccountUseCase
import com.upsaclay.app.domain.usecase.FcmTokenUseCase
import com.upsaclay.app.domain.usecase.FetchDataUseCase
import com.upsaclay.app.domain.usecase.ListenBlockedUserEventsUseCase
import com.upsaclay.app.domain.usecase.ListenDataUseCase
import com.upsaclay.app.domain.usecase.ListenRemoteUserUseCase
import com.upsaclay.app.domain.usecase.LogoutUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val BACKGROUND_SCOPE = named("BackgroundScope")

val appDomainModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
            SupervisorJob() +
                    Dispatchers.IO +
                    CoroutineExceptionHandler { _, throwable ->
                        System.err.print("Uncaught error in backgroundScope: $throwable")
                    }
        )
    }

    singleOf(::ClearDataUseCase)
    singleOf(::DeleteAccountUseCase)
    singleOf(::FetchDataUseCase)
    singleOf(::DeleteAccountUseCase)
    single {
        FcmTokenUseCase(
            userRepository = get(),
            fcmTokenRepository = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::ListenRemoteUserUseCase)
    singleOf(::ListenBlockedUserEventsUseCase)
    singleOf(::LogoutUseCase)
    single {
        ListenDataUseCase(
            listenRemoteUserUseCase = get(),
            listenRemoteConversationsUseCase = get(),
            listenRemoteMessagesUseCase = get(),
            listenBlockedUserEventsUseCase = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
}