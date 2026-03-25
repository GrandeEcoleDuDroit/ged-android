package com.upsaclay.mission.data.remote.models

import com.upsaclay.common.data.remote.model.RemoteReporter

data class RemoteMissionReport(
    val missionId: String,
    val reporter: RemoteReporter,
    val reason: String
)
