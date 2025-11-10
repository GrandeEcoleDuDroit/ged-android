package com.upsaclay.news.domain

import com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.domain.usecase.RefreshAnnouncementsUseCase
import com.upsaclay.news.domain.usecase.ResendAnnouncementUseCase
import com.upsaclay.news.domain.usecase.SynchronizeAnnouncementsUseCase
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
                        System.err.print("Uncaught error in backgroundScope: $throwable")
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
        ResendAnnouncementUseCase(
            announcementRepository = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::RefreshAnnouncementsUseCase)
    singleOf(::DeleteAnnouncementUseCase)
    singleOf(::SynchronizeAnnouncementsUseCase)
}