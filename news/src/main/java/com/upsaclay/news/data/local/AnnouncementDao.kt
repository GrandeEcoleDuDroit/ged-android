package com.upsaclay.news.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface AnnouncementDao {
    @Query("SELECT * FROM $ANNOUNCEMENTS_LOCAL_TABLE_NAME")
    fun getAllAnnouncements(): List<AnnouncementEntity>

    @Upsert
    fun upsertAnnouncement(announcementEntity: AnnouncementEntity)
}