package com.upsaclay.mission.data.remote.api

import com.upsaclay.common.data.remote.model.OracleUser
import com.upsaclay.mission.data.remote.models.InboundRemoteMission
import com.upsaclay.mission.data.remote.models.OutboundRemoteMission
import com.upsaclay.mission.data.remote.models.RemoteMissionReport
import java.io.File

interface MissionApi {
    suspend fun getMissions():  List<InboundRemoteMission>?

    suspend fun createMission(remoteMission: OutboundRemoteMission, imageFile: File?)

    suspend fun updateMission(remoteMission: OutboundRemoteMission, imageFile: File?)

    suspend fun deleteMission(remoteMission: OutboundRemoteMission)

    suspend fun reportMission(remoteMissionReport: RemoteMissionReport)

    suspend fun addParticipant(missionId: String, oracleUser: OracleUser)

    suspend fun removeParticipant(missionId: String, userId: String)
}