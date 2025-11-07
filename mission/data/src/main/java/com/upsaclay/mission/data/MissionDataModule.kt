package com.upsaclay.mission.data

import com.upsaclay.common.data.GED_SERVER_QUALIFIER
import com.upsaclay.common.data.e
import com.upsaclay.mission.data.local.MissionLocalDataSource
import com.upsaclay.mission.data.remote.MissionRemoteDataSource
import com.upsaclay.mission.data.remote.api.MissionApi
import com.upsaclay.mission.data.remote.api.MissionApiImpl
import com.upsaclay.mission.data.remote.api.ServerMissionApi
import com.upsaclay.mission.data.repositories.MissionRepositoryImpl
import com.upsaclay.mission.domain.repository.MissionRepository
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

val missionDataModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
            SupervisorJob() +
                    Dispatchers.IO +
                    CoroutineExceptionHandler { _, throwable ->
                        e("Uncaught error in backgroundScope: ${throwable.message}")
                    }
        )
    }

    single {
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(ServerMissionApi::class.java)
    }
    singleOf(::MissionApiImpl) { bind<MissionApi>() }
    singleOf(::MissionLocalDataSource)
    singleOf(::MissionRemoteDataSource)
    single<MissionRepository> {
        MissionRepositoryImpl(
            missionLocalDataSource = get(),
            missionRemoteDataSource = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
}