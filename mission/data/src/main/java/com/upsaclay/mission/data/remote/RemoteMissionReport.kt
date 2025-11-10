package com.upsaclay.mission.data.remote

data class RemoteMissionReport(
    val missionId: String,
    val userInfo: RemoteMissionReport.RemoteUserInfo,
    val reason: String
) {
    data class RemoteUserInfo(
        val fullName: String,
        val email: String
    )
}
