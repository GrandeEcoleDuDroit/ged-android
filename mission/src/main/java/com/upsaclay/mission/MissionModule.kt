package com.upsaclay.mission

import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.presentation.MissionViewModel
import com.upsaclay.mission.presentation.createmission.CreateMissionViewModel
import com.upsaclay.mission.presentation.editmission.EditMissionViewModel
import com.upsaclay.mission.presentation.missiondetails.MissionDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val missionModule = module {
    viewModelOf(::MissionViewModel)
    viewModelOf(::CreateMissionViewModel)
    viewModel { (missionId: String) ->
        MissionDetailsViewModel(
            missionId = missionId,
            missionRepository = get(),
            userRepository = get(),
            connectivityObserver = get(),
            deleteMissionUseCase = get()
        )
    }
    viewModel { (mission: Mission) ->
        EditMissionViewModel(
            mission = mission,
            updateMissionUseCase = get(),
            connectivityObserver = get(),
            getUsersUseCase = get(),
            generateIdUseCase = get()
        )
    }
}