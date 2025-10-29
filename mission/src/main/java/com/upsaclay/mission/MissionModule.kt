package com.upsaclay.mission

import com.upsaclay.mission.presentation.MissionViewModel
import com.upsaclay.mission.presentation.createmission.CreateMissionViewModel
import com.upsaclay.mission.presentation.seemission.SeeMissionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val missionModule = module {
    viewModelOf(::MissionViewModel)
    viewModelOf(::CreateMissionViewModel)
    viewModel { (missionId: Int) ->
        SeeMissionViewModel(
            missionId = missionId,
            missionRepository = get()
        )
    }
}