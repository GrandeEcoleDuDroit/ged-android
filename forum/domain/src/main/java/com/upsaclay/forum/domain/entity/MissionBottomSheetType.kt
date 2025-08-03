package com.upsaclay.forum.domain.entity

sealed class MissionBottomSheetType {
    data object AddTask : MissionBottomSheetType()
    data class EditTask(val task: Task) : MissionBottomSheetType()
    data object SelectManager : MissionBottomSheetType()
    data object ModifyImage : MissionBottomSheetType()
}