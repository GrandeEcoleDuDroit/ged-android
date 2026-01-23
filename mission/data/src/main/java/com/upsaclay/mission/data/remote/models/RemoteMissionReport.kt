package com.upsaclay.mission.data.remote.models

data class RemoteMissionReport(
    val missionId: String,
    val reporter: RemoteReporter,
    val reason: String
) {
    data class RemoteReporter(
        val fullName: String,
        val email: String
    )
}
