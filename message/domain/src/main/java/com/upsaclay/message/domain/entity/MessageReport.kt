package com.upsaclay.message.domain.entity

data class MessageReport(
    val conversationId: String,
    val messageId: String,
    val recipientInfo: MessageReport.UserInfo,
    val reason: MessageReport.Reason
) {
    data class UserInfo(
        val fullName: String,
        val email: String
    )

    enum class Reason {
        NUDITY_OR_SEXUAL_CONTENT,
        HATE_SPEECH_OR_SYMBOL,
        SPAM,
        BULLYING_OR_HARASSMENT,
        ILLEGAL_CONTENT,
        SCAM_OR_FRAUD,
        OTHER;

        override fun toString(): String {
            return when (this) {
                NUDITY_OR_SEXUAL_CONTENT -> "Nudity or sexual content"
                HATE_SPEECH_OR_SYMBOL -> "Hate speech or symbol"
                ILLEGAL_CONTENT -> "Illegal content"
                SPAM -> "Spam"
                BULLYING_OR_HARASSMENT -> "Bullying or harassment"
                SCAM_OR_FRAUD -> "Scam or fraud"
                OTHER -> "Other"
            }
        }
    }
}