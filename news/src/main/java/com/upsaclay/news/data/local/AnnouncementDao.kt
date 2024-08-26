package com.upsaclay.news.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {
    @Query("SELECT * FROM $ANNOUNCEMENTS_LOCAL_TABLE_NAME")
    fun getAllAnnouncements(): Flow<List<AnnouncementEntity>>

    @Upsert
    fun upsertAnnouncement(announcementEntity: AnnouncementEntity)

    @Delete
    fun deleteAnnouncement(announcementEntity: AnnouncementEntity)
}