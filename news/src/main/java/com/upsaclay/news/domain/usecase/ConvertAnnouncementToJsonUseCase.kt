package com.upsaclay.news.domain.usecase

import com.google.gson.Gson
import com.upsaclay.news.data.local.LocalAnnouncement
import com.upsaclay.news.domain.model.Announcement

class ConvertAnnouncementToJsonUseCase {
    private val gson = Gson()

    fun toJson(announcement: Announcement): String {
        val announcementLocal = LocalAnnouncement.fromDomain(announcement)
        return gson.toJson(announcementLocal)
    }

    fun fromJson(jsonAnnouncement: String): Announcement {
        val announcementLocal = gson.fromJson(jsonAnnouncement, LocalAnnouncement::class.java)
        return announcementLocal.toDomain()
    }
}