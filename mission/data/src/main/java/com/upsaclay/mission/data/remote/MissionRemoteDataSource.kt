package com.upsaclay.mission.data.remote

import com.upsaclay.mission.data.remote.api.MissionApi
import com.upsaclay.mission.domain.entity.Mission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MissionRemoteDataSource(private val missionApi: MissionApi) {
    suspend fun getMissions(): List<Mission> = withContext(Dispatchers.IO) {
        missionApi.getMissions()
    }

    suspend fun createMission(mission: Mission, imageFile: File?) {
        withContext(Dispatchers.IO) {
            missionApi.createMission(mission, imageFile)
        }
    }

    suspend fun updateMission(mission: Mission, imageFile: File?) {
        withContext(Dispatchers.IO) {
            missionApi.updateMission(mission, imageFile)
        }
    }

    suspend fun deleteMission(missionId: Long, imageUrl: String?) {
        withContext(Dispatchers.IO) {
            missionApi.deleteMission(missionId, imageUrl)
        }
    }
}