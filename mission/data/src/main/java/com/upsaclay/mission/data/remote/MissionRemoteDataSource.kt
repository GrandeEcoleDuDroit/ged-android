package com.upsaclay.mission.data.remote

import com.upsaclay.common.data.toOracleUser
import com.upsaclay.common.domain.entity.User
import com.upsaclay.mission.data.mapper.toMission
import com.upsaclay.mission.data.mapper.toRemote
import com.upsaclay.mission.data.remote.api.MissionApi
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MissionRemoteDataSource(private val missionApi: MissionApi) {
    suspend fun getMissions(): List<Mission> = withContext(Dispatchers.IO) {
        missionApi.getMissions()?.map { it.toMission() } ?: emptyList()
    }

    suspend fun createMission(mission: Mission, imageFile: File?) {
        withContext(Dispatchers.IO) {
            missionApi.createMission(mission.toRemote(), imageFile)
        }
    }

    suspend fun updateMission(mission: Mission, imageFile: File?) {
        withContext(Dispatchers.IO) {
            missionApi.updateMission(mission.toRemote(), imageFile)
        }
    }

    suspend fun deleteMission(mission: Mission) {
        withContext(Dispatchers.IO) {
            missionApi.deleteMission(mission.toRemote())
        }
    }

    suspend fun reportMission(report: MissionReport) {
        withContext(Dispatchers.IO) {
            missionApi.reportMission(report.toRemote())
        }
    }

    suspend fun addParticipant(missionId: String, user: User) {
        withContext(Dispatchers.IO) {
            missionApi.addParticipant(missionId, user.toOracleUser())
        }
    }

    suspend fun removeParticipant(missionId: String, userId: String) {
        withContext(Dispatchers.IO) {
            missionApi.removeParticipant(missionId, userId)
        }
    }
}