package com.upsaclay.news

import com.upsaclay.news.data.AnnouncementApi
import com.upsaclay.news.data.AnnouncementRemoteDataSource
import com.upsaclay.news.data.AnnouncementRepository
import com.upsaclay.news.data.AnnouncementRepositoryImpl
import com.upsaclay.news.domain.GetAllAnnouncementUseCase
import com.upsaclay.news.domain.UpdateAnnouncementsUseCase
import com.upsaclay.news.ui.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit

val newsModule = module {
    single { get<Retrofit>().create(AnnouncementApi::class.java) }

    singleOf(::AnnouncementRepositoryImpl) { bind<AnnouncementRepository>() }
    singleOf(::AnnouncementRemoteDataSource)
    viewModelOf(::NewsViewModel)

    singleOf(::GetAllAnnouncementUseCase)
    singleOf(::UpdateAnnouncementsUseCase)
}