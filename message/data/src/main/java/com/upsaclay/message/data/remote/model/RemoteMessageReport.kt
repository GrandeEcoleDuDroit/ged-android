package com.upsaclay.message.data.remote.model

internal data class RemoteMessageReport(
    val conversationId: String,
    val messageId: Long,
    val recipientInfo: RemoteMessageReport.RemoteUserInfo,
    val reason: String
) {
    internal data class RemoteUserInfo(
        val fullName: String,
        val email: String
    )
}
