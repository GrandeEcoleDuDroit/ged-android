package com.upsaclay.app.data

import androidx.room.Room
import com.upsaclay.app.data.local.FcmDataStore
import com.upsaclay.app.data.local.FcmLocalDataSource
import com.upsaclay.app.data.repository.FcmTokenRepositoryImpl
import com.upsaclay.app.data.repository.RouteRepositoryImpl
import com.upsaclay.app.domain.repository.FcmTokenRepository
import com.upsaclay.common.data.GED_SERVER_QUALIFIER
import com.upsaclay.common.data.remote.api.FcmApi
import com.upsaclay.common.domain.repository.RouteRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit

private const val DATABASE_NAME = "GedoiseDatabase"

val appDataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            GedoiseDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    single { get<GedoiseDatabase>().announcementDao() }
    single { get<GedoiseDatabase>().conversationDao() }
    single { get<GedoiseDatabase>().messageDao() }
    single { get<GedoiseDatabase>().conversationMessageDao() }
    single { get<GedoiseDatabase>().messageNotificationDao() }
    single { get<GedoiseDatabase>().missionDao() }

    single {
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(FcmApi::class.java)
    }

    singleOf(::RouteRepositoryImpl) { bind<RouteRepository>() }
    singleOf(::FcmLocalDataSource)
    singleOf(::FcmDataStore)
    singleOf(::FcmTokenRepositoryImpl) { bind<FcmTokenRepository>() }
}