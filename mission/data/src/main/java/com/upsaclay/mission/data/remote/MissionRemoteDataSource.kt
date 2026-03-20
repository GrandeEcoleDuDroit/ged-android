package com.upsaclay.mission.data.remote

import com.upsaclay.common.data.exceptions.mapServerException
import com.upsaclay.common.data.toOracleUser
import com.upsaclay.common.domain.entity.User
import com.upsaclay.mission.data.mapper.toMission
import com.upsaclay.mission.data.mapper.toRemote
import com.upsaclay.mission.data.remote.api.MissionApi
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionReport
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MissionRemoteDataSource(private val missionApi: MissionApi) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    suspend fun getMissions(): List<Mission> = withContext(dispatcher) {
        try {
            missionApi.getMissions()?.map { it.toMission() } ?: emptyList()
        } catch (e: Exception) {
            throw mapServerException(e)
        }
    }

    suspend fun createMission(mission: Mission, imageFile: File?) {
        withContext(dispatcher) {
            try {
                missionApi.createMission(mission.toRemote(), imageFile)
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun updateMission(user: User, mission: Mission, imageFile: File?) {
        withContext(dispatcher) {
            try {
                missionApi.updateMission(user.id, mission.toRemote(), imageFile)
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun deleteMission(mission: Mission) {
        withContext(dispatcher) {
            try {
                missionApi.deleteMission(mission.id)
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun reportMission(report: MissionReport) {
        withContext(dispatcher) {
            try {
                missionApi.reportMission(report.toRemote())
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun addParticipant(missionId: String, user: User) {
        withContext(dispatcher) {
            try {
                missionApi.addParticipant(missionId, user.toOracleUser())
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }

    suspend fun removeParticipant(missionId: String, userId: String) {
        withContext(dispatcher) {
            try {
                missionApi.removeParticipant(missionId, userId)
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }
}