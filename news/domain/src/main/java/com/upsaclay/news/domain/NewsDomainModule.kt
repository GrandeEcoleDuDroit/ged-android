package com.upsaclay.news.domain

import com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.domain.usecase.RefreshAnnouncementUseCase
import com.upsaclay.news.domain.usecase.ResendAnnouncementUseCase
import com.upsaclay.news.domain.usecase.SynchronizeAnnouncementsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val newsDomainModule = module {
    singleOf(::CreateAnnouncementUseCase)
    singleOf(::ResendAnnouncementUseCase)
    singleOf(::RefreshAnnouncementUseCase)
    singleOf(::DeleteAnnouncementUseCase)
    singleOf(::SynchronizeAnnouncementsUseCase)
}