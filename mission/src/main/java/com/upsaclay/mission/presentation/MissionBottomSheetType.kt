package com.upsaclay.mission.presentation

import com.upsaclay.mission.domain.entity.MissionTask

sealed class MissionBottomSheetType {
    data object AddTask : MissionBottomSheetType()
    data class EditTask(val missionTask: MissionTask) : MissionBottomSheetType()
    data object SelectManager : MissionBottomSheetType()
}