package com.upsaclay.mission

import com.upsaclay.mission.presentation.MissionViewModel
import com.upsaclay.mission.presentation.createmission.CreateMissionViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val missionModule = module {
    viewModelOf(::MissionViewModel)
    viewModelOf(::CreateMissionViewModel)
}