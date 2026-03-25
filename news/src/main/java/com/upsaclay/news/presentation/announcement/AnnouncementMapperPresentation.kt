package com.upsaclay.news.presentation.announcement

import com.upsaclay.news.domain.announcement.AnnouncementReport

val AnnouncementReport.Reason.stringRes: Int
    get() = when (this) {
        AnnouncementReport.Reason.SELLING_PROMOTING_INAPPROPRIATE_ITEMS ->
            com.upsaclay.common.R.string.selling_promoting_inappropriate_content_report_reason

        AnnouncementReport.Reason.VIOLENT_HATEFUL_CONTENT ->
            com.upsaclay.common.R.string.violent_hateful_content_report_reason

        AnnouncementReport.Reason.SPAM_SCAM -> com.upsaclay.common.R.string.spam_scam_report_reason

        AnnouncementReport.Reason.FALSE_INFORMATION -> com.upsaclay.common.R.string.false_information_report_reason

        AnnouncementReport.Reason.INTELLECTUAL_PROPERTY_VIOLATION ->
            com.upsaclay.common.R.string.intellectual_property_violation_report_reason
    }