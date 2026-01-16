package com.upsaclay.mission.data.remote

import com.upsaclay.common.data.exceptions.mapServerException
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
        try {
            missionApi.getMissions()?.map { it.toMission() } ?: emptyList()
        } catch (e: Exception) {
            throw mapServerException(e)
        }
    }

    suspend fun createMission(mission: Mission, imageFile: File?) {
        withContext(Dispatchers.IO) {
            try {
                missionApi.createMission(mission.toRemote(), imageFile)
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun updateMission(mission: Mission, imageFile: File?) {
        withContext(Dispatchers.IO) {
            try {
                missionApi.updateMission(mission.toRemote(), imageFile)
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun deleteMission(mission: Mission) {
        withContext(Dispatchers.IO) {
            try {
                missionApi.deleteMission(mission.toRemote())
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun reportMission(report: MissionReport) {
        withContext(Dispatchers.IO) {
            try {
                missionApi.reportMission(report.toRemote())
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun addParticipant(missionId: String, user: User) {
        withContext(Dispatchers.IO) {
            try {
                missionApi.addParticipant(missionId, user.toOracleUser())
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun removeParticipant(missionId: String, userId: String) {
        withContext(Dispatchers.IO) {
            try {
                missionApi.removeParticipant(missionId, userId)
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }
}