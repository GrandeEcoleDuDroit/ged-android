package com.upsaclay.message.data.remote.model

internal data class RemoteMessageReport(
    val conversationId: String,
    val messageId: String,
    val recipient: RemoteMessageReport.RemoteRecipient,
    val reason: String
) {
    internal data class RemoteRecipient(
        val fullName: String,
        val email: String
    )
}
