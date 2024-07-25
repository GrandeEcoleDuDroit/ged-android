package com.upsaclay.news

import com.upsaclay.core.GEDOISE_VM_1_QUALIFIER
import com.upsaclay.news.data.remote.AnnouncementApi
import com.upsaclay.news.data.remote.AnnouncementRemoteDataSource
import com.upsaclay.news.data.AnnouncementRepository
import com.upsaclay.news.data.AnnouncementRepositoryImpl
import com.upsaclay.news.domain.GetAllAnnouncementUseCase
import com.upsaclay.news.domain.UpdateAnnouncementsUseCase
import com.upsaclay.news.ui.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val newsModule = module {
    single { get<Retrofit>(qualifier = named(GEDOISE_VM_1_QUALIFIER)).create(AnnouncementApi::class.java) }

    singleOf(::AnnouncementRepositoryImpl) { bind<AnnouncementRepository>() }
    singleOf(::AnnouncementRemoteDataSource)
    viewModelOf(::NewsViewModel)

    singleOf(::GetAllAnnouncementUseCase)
    singleOf(::UpdateAnnouncementsUseCase)
}