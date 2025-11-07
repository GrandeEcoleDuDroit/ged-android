package com.upsaclay.mission.data.local

import com.upsaclay.mission.data.toLocal
import com.upsaclay.mission.data.toMission
import com.upsaclay.mission.domain.entity.Mission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext

class MissionLocalDataSource(private val missionDao: MissionDao) {
    fun getMissions(): Flow<List<Mission>> = missionDao.getMissions()
        .map { localMissions ->
            localMissions.map { it.toMission() }
        }

    fun getMissionFlow(missionId: Long): Flow<Mission> =
        missionDao.getMissionFlow(missionId).mapNotNull { it?.toMission() }

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
}