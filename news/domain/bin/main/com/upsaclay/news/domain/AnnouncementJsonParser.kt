package com.upsaclay.news.domain

import com.google.gson.GsonBuilder
import com.upsaclay.common.domain.adapter.LocalDateTimeAdapter
import com.upsaclay.news.domain.entity.Announcement
import java.time.LocalDateTime

object AnnouncementJsonParser {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
        .create()

    fun toAnnouncement(announcementJson: String): Announcement = gson.fromJson(announcementJson, Announcement::class.java)

    fun toJson(announcement: Announcement): String = gson.toJson(announcement)
}