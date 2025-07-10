package com.upsaclay.news.domain.entity

import com.upsaclay.common.domain.LocalDateTimeSerializer
import com.upsaclay.common.domain.entity.User
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@InternalSerializationApi @Serializable
data class Announcement(
    val id: String,
    val title: String? = null,
    val content: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
    val author: User,
    val state: AnnouncementState
)

enum class AnnouncementState {
    DRAFT,
    PUBLISHING,
    PUBLISHED,
    ERROR
}