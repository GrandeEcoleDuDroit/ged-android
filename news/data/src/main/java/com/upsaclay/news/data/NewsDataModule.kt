package com.upsaclay.news.data

import com.upsaclay.common.data.SERVER_1_RETROFIT_QUALIFIER
import com.upsaclay.news.data.local.AnnouncementLocalDataSource
import com.upsaclay.news.data.remote.AnnouncementRemoteDataSource
import com.upsaclay.news.data.remote.api.AnnouncementApi
import com.upsaclay.news.data.repository.AnnouncementRepositoryImpl
import com.upsaclay.news.domain.repository.AnnouncementRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val newsDataModule = module {
    single {
        get<Retrofit>(qualifier = named(SERVER_1_RETROFIT_QUALIFIER))
            .create(AnnouncementApi::class.java)
    }

    singleOf(::AnnouncementRepositoryImpl) { bind<AnnouncementRepository>() }
    singleOf(::AnnouncementRemoteDataSource)
    singleOf(::AnnouncementLocalDataSource)
}