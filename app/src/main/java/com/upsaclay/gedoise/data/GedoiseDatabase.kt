package com.upsaclay.gedoise.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.upsaclay.news.data.local.AnnouncementDao
import com.upsaclay.news.data.local.AnnouncementEntity

@Database(entities = [AnnouncementEntity::class], version = 1)
abstract class GedoiseDatabase: RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
}