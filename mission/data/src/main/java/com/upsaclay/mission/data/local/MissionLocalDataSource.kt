package com.upsaclay.mission.data.local

import android.content.Context
import com.upsaclay.common.domain.entity.User
import com.upsaclay.mission.data.mapper.toLocal
import com.upsaclay.mission.data.mapper.toMission
import com.upsaclay.mission.domain.MissionUtils
import com.upsaclay.mission.domain.entity.Mission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import java.io.File

class MissionLocalDataSource(
    private val context: Context,
    private val missionDao: MissionDao
) {
    fun getMissionsFlow(): Flow<List<Mission>> = missionDao.getMissionsFlow()
        .map { localMissions ->
            localMissions.map { it.toMission(::getImagePath) }
        }

    fun getMissionFlow(missionId: String): Flow<Mission> =
        missionDao.getMissionFlow(missionId).mapNotNull { it?.toMission(::getImagePath) }

    suspend fun getMissions(): List<Mission> =
        missionDao.getMissions().map { it.toMission(::getImagePath) }

    suspend fun getMission(missionId: String): Mission? =
        missionDao.getMission(missionId)?.toMission(::getImagePath)

    suspend fun upsertMission(mission: Mission) {
        withContext(Dispatchers.IO) {
            missionDao.upsertMission(mission.toLocal())
        }
    }

    suspend fun deleteMission(mission: Mission) {
        withContext(Dispatchers.IO) {
            missionDao.deleteMission(mission.toLocal())
        }
    }

    suspend fun addParticipant(missionId: String, user: User) {
        withContext(Dispatchers.IO) {
            missionDao.getMission(missionId)?.let { localMission ->
                val mission = localMission.toMission(::getImagePath).let { mission ->
                    mission.copy(participants = mission.participants.toMutableList().apply { add(user) })
                }
                missionDao.upsertMission(mission.toLocal())
            }
        }
    }

    suspend fun removeParticipant(missionId: String, userId: String) {
        withContext(Dispatchers.IO) {
            missionDao.getMission(missionId)?.let { localMission ->
                val mission = localMission.toMission(::getImagePath).let { mission ->
                    mission.copy(participants = mission.participants.filter { it.id != userId })
                }
                missionDao.upsertMission(mission.toLocal())
            }
        }
    }

    private fun getImagePath(fileName: String): String =
        File(context.filesDir, MissionUtils.Image.getRelativePath(fileName)).path
}