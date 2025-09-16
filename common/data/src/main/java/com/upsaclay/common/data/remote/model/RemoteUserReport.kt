package com.upsaclay.common.data.remote.model

data class RemoteUserReport(
    val userId: String,
    val userInfo: RemoteUserReport.RemoteUserInfo,
    val reporterInfo: RemoteUserReport.RemoteUserInfo,
    val reason: String
) {
    data class RemoteUserInfo(
        val fullName: String,
        val email: String
    )
}
