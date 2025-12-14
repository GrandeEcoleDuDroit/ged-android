package com.upsaclay.common.data.remote.model

data class RemoteUserReport(
    val user: RemoteReportedUser,
    val reporter: RemoteReporter,
    val reason: String
) {
    data class RemoteReportedUser(
        val id: String,
        val fullName: String,
        val email: String
    )

    data class RemoteReporter(
        val fullName: String,
        val email: String
    )
}
