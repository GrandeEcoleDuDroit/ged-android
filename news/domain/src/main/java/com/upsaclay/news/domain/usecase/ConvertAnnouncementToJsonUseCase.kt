package com.upsaclay.news.domain.usecase

import com.google.gson.GsonBuilder
import com.upsaclay.common.domain.usecase.LocalDateTimeSerializer
import com.upsaclay.news.domain.model.Announcement
import java.time.LocalDateTime

class ConvertAnnouncementToJsonUseCase {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeSerializer())
        .create()

    fun toJson(announcement: Announcement): String {
        return gson.toJson(announcement)
    }

    fun fromJson(jsonAnnouncement: String): Announcement {
        return gson.fromJson(jsonAnnouncement, Announcement::class.java)
    }
}