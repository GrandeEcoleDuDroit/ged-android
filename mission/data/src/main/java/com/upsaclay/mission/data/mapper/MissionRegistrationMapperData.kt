package com.upsaclay.mission.data.mapper

import com.upsaclay.mission.data.remote.models.RemoteAddMissionParticipant
import com.upsaclay.mission.domain.entity.AddMissionParticipant

fun AddMissionParticipant.toRemote() = RemoteAddMissionParticipant(
    missionId = missionId,
    missionSchoolLevels = schoolLevels.map { it.number },
    missionMaxParticipants = maxParticipants,
    missionParticipantsNumber = participantsNumber,
    userId = currentUser.id,
    userSchoolLevel = currentUser.schoolLevel.number
)