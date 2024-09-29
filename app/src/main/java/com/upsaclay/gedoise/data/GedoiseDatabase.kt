package com.upsaclay.gedoise.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.upsaclay.message.data.local.dao.ConversationDao
import com.upsaclay.message.data.local.dao.MessageDao
import com.upsaclay.news.data.local.AnnouncementDao
import com.upsaclay.news.data.local.LocalAnnouncement

@Database(entities = [LocalAnnouncement::class], version = 3)
internal abstract class GedoiseDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
}