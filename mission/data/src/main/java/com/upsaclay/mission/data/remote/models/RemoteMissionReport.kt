package com.upsaclay.mission.data.remote.models

data class RemoteMissionReport(
    val missionId: String,
    val userInfo: RemoteUserInfo,
    val reason: String
) {
    data class RemoteUserInfo(
        val fullName: String,
        val email: String
    )
}
