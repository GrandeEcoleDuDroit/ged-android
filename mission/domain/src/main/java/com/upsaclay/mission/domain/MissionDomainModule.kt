package com.upsaclay.mission.domain

import com.upsaclay.mission.domain.usecase.CreateMissionUseCase
import com.upsaclay.mission.domain.usecase.DeleteMissionUseCase
import com.upsaclay.mission.domain.usecase.FetchMissionsUseCase
import com.upsaclay.mission.domain.usecase.RecreateMissionUseCase
import com.upsaclay.mission.domain.usecase.RefreshMissionsUseCase
import com.upsaclay.mission.domain.usecase.UpdateMissionUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val BACKGROUND_SCOPE = named("BackgroundScope")

val missionDomainModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
            SupervisorJob() +
                    Dispatchers.IO +
                    CoroutineExceptionHandler { _, throwable ->
                        System.err.print("Uncaught error in backgroundScope: ${throwable.message}")
                    }
        )
    }

    single {
        CreateMissionUseCase(
            missionRepository = get(),
            imageRepository = get(),
            missionJobQueue = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }

    single {
        RecreateMissionUseCase(
            missionRepository = get(),
            fileRepository = get(),
            imageRepository = get(),
            missionJobQueue = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }

    singleOf(::DeleteMissionUseCase)
    singleOf(::UpdateMissionUseCase)
    singleOf(::FetchMissionsUseCase)
    singleOf(::RefreshMissionsUseCase)
    singleOf(::MissionJobQueue)
}