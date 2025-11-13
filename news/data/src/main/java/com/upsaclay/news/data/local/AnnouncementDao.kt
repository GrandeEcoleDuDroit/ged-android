package com.upsaclay.news.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_AUTHOR_ID
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_DATE
import com.upsaclay.news.data.AnnouncementField.Local.ANNOUNCEMENT_TABLE_NAME
import com.upsaclay.news.data.local.model.LocalAnnouncement
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {
    @Query("SELECT * FROM $ANNOUNCEMENT_TABLE_NAME ORDER BY $ANNOUNCEMENT_DATE DESC")
    fun getAnnouncements(): Flow<List<LocalAnnouncement>>

    @Upsert
    suspend fun upsertAnnouncement(localAnnouncement: LocalAnnouncement)

    @Delete
    suspend fun deleteAnnouncement(localAnnouncement: LocalAnnouncement)

    @Query("DELETE FROM $ANNOUNCEMENT_TABLE_NAME")
    suspend fun deleteAnnouncements()

    @Query("DELETE FROM $ANNOUNCEMENT_TABLE_NAME WHERE $ANNOUNCEMENT_AUTHOR_ID = :userId")
    suspend fun deleteAnnouncements(userId: String)
}