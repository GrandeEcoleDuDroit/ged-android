package com.upsaclay.news.data

import com.upsaclay.common.data.GED_SERVER_QUALIFIER
import com.upsaclay.common.data.utils.e
import com.upsaclay.news.data.announcement.AnnouncementRepositoryImpl
import com.upsaclay.news.data.announcement.local.AnnouncementLocalDataSource
import com.upsaclay.news.data.announcement.remote.AnnouncementApi
import com.upsaclay.news.data.announcement.remote.AnnouncementRemoteDataSource
import com.upsaclay.news.data.post.PostRepositoryImpl
import com.upsaclay.news.data.post.local.PostLocalDataSource
import com.upsaclay.news.data.post.remote.PostApi
import com.upsaclay.news.data.post.remote.PostApiImpl
import com.upsaclay.news.data.post.remote.PostRemoteDataSource
import com.upsaclay.news.data.post.remote.PostServerApi
import com.upsaclay.news.domain.announcement.AnnouncementRepository
import com.upsaclay.news.domain.post.PostRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

private val BACKGROUND_SCOPE = named("BackgroundScope")

val newsDataModule = module {
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
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(AnnouncementApi::class.java)
    }

    singleOf(::AnnouncementRepositoryImpl) { bind<AnnouncementRepository>() }
    singleOf(::AnnouncementRemoteDataSource)
    singleOf(::AnnouncementLocalDataSource)

    single {
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(PostServerApi::class.java)
    }

    singleOf(::PostApiImpl) { bind<PostApi>() }
    singleOf(::PostRepositoryImpl) { bind<PostRepository>() }
    singleOf(::PostRemoteDataSource)
    singleOf(::PostLocalDataSource)
}