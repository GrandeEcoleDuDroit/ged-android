package com.upsaclay.news.domain

import com.upsaclay.news.domain.announcement.AnnouncementJobQueue
import com.upsaclay.news.domain.announcement.usecase.CreateAnnouncementUseCase
import com.upsaclay.news.domain.announcement.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.domain.announcement.usecase.FetchAnnouncementsUseCase
import com.upsaclay.news.domain.announcement.usecase.RecreateAnnouncementUseCase
import com.upsaclay.news.domain.announcement.usecase.RefreshAnnouncementsUseCase
import com.upsaclay.news.domain.post.usecase.CreatePostUseCase
import com.upsaclay.news.domain.post.usecase.DeletePostUseCase
import com.upsaclay.news.domain.post.usecase.FetchPostsUseCase
import com.upsaclay.news.domain.post.usecase.UpsertLocalPostUseCase
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
            announcementJobQueue = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    single {
        RecreateAnnouncementUseCase(
            announcementRepository = get(),
            announcementJobQueue = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::RefreshAnnouncementsUseCase)
    singleOf(::DeleteAnnouncementUseCase)
    singleOf(::FetchAnnouncementsUseCase)
    singleOf(::AnnouncementJobQueue)

    single {
        CreatePostUseCase(
            postRepository = get(),
            imageRepository = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::UpsertLocalPostUseCase)
    singleOf(::FetchPostsUseCase)
    singleOf(::DeletePostUseCase)
}