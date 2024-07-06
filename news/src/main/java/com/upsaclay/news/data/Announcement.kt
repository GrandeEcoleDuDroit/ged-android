package com.upsaclay.news.data

import android.graphics.Bitmap
import java.time.LocalDate

data class Announcement(
    val title: String,
    val authorPicture: Bitmap,
    val authorName: String,
    val content: String,
    val date: String
)

data class AnnouncementDTO(
    val authorPicture: ByteArray,
    val authorName: String,
    val description: String,
    val date: LocalDate
)