package com.upsaclay.mission.domain

import com.upsaclay.mission.domain.usecase.CreateMissionUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
            fileRepository = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
}