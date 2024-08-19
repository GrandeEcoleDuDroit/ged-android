package com.upsaclay.news.domain.model

import com.upsaclay.common.domain.model.User
import java.time.LocalDateTime

data class Announcement(
    val id: Int,
    val title: String,
    val content: String,
    val date: LocalDateTime,
    val author: User
)
