package com.upsaclay.message.presentation

import com.upsaclay.message.R
import com.upsaclay.message.domain.entity.MessageReport

val MessageReport.Reason.stringRes: Int
    get() = when (this) {
        MessageReport.Reason.NUDITY_OR_SEXUAL_CONTENT -> R.string.nudity_or_sexual_content
        MessageReport.Reason.HATE_SPEECH_OR_SYMBOL -> R.string.hate_speech_or_symbols
        MessageReport.Reason.SPAM -> R.string.spam
        MessageReport.Reason.BULLYING_OR_HARASSMENT -> R.string.bullying_or_harassment
        MessageReport.Reason.ILLEGAL_CONTENT -> R.string.illegal_content
        MessageReport.Reason.SCAM_OR_FRAUD -> R.string.scam_or_fraud
    }