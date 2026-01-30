package com.upsaclay.news.domain.entity

data class AnnouncementReport(
    val announcementId: String,
    val author: Author,
    val reporter: Reporter,
    val reason: String
) {
    data class Author(
        val fullName: String,
        val email: String
    )

    data class Reporter(
        val fullName: String,
        val email: String
    )

    enum class Reason {
        SELLING_PROMOTING_INAPPROPRIATE_ITEMS,
        VIOLENT_HATEFUL_CONTENT,
        SPAM_SCAM,
        FALSE_INFORMATION,
        INTELLECTUAL_PROPERTY_VIOLATION
    }
}
