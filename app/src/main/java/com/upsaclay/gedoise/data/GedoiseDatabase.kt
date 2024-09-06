package com.upsaclay.gedoise.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.upsaclay.news.data.local.AnnouncementDao
import com.upsaclay.news.data.local.LocalAnnouncement

@Database(entities = [LocalAnnouncement::class], version = 2)
internal abstract class GedoiseDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
}