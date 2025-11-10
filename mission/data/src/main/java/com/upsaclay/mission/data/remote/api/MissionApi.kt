package com.upsaclay.mission.data.remote.api

import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionReport
import java.io.File

interface MissionApi {
    suspend fun getMissions(): List<Mission>

    suspend fun createMission(mission: Mission, imageFile: File?)

    suspend fun updateMission(mission: Mission, imageFile: File?)

    suspend fun deleteMission(missionId: String, imageFileName: String?)

    suspend fun reportMission(report: MissionReport)
}