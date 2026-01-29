package com.upsaclay.message.domain.entity

data class MessageReport(
    val conversationId: String,
    val messageId: String,
    val recipient: MessageReport.Recipient,
    val reason: String
) {
    data class Recipient(
        val fullName: String,
        val email: String
    )

    enum class Reason {
        NUDITY_OR_SEXUAL_CONTENT,
        HATE_SPEECH_OR_SYMBOL,
        SPAM,
        BULLYING_OR_HARASSMENT,
        ILLEGAL_CONTENT,
        SCAM_OR_FRAUD
    }
}