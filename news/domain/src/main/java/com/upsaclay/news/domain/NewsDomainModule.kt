package com.upsaclay.news.domain

import com.upsaclay.common.domain.e
import com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.domain.usecase.ListenBlockUserEvents
import com.upsaclay.news.domain.usecase.RefreshAnnouncementUseCase
import com.upsaclay.news.domain.usecase.ResendAnnouncementUseCase
import com.upsaclay.news.domain.usecase.UpdateAnnouncementUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val BACKGROUND_SCOPE = named("BackgroundScope")

val newsDomainModule = module {
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
        CreateAnnouncementUseCase(
            announcementRepository = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }

    single {
        ListenBlockUserEvents(
            blockedUserRepository = get(),
            announcementRepository = get(),
            scope = get(BACKGROUND_SCOPE)
        ).start()
    }

    single {
        ResendAnnouncementUseCase(
            announcementRepository = get(),
            connectivityObserver = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }

    singleOf(::DeleteAnnouncementUseCase)
    singleOf(::RefreshAnnouncementUseCase)
    singleOf(::UpdateAnnouncementUseCase)
}