package com.upsaclay.app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.upsaclay.message.data.local.dao.ConversationDao
import com.upsaclay.message.data.local.dao.ConversationMessageDao
import com.upsaclay.message.data.local.dao.MessageDao
import com.upsaclay.message.data.local.dao.NotificationMessageDao
import com.upsaclay.message.data.local.model.LocalConversation
import com.upsaclay.message.data.local.model.LocalConversationMessage
import com.upsaclay.message.data.local.model.LocalMessage
import com.upsaclay.message.data.local.model.LocalNotificationMessage
import com.upsaclay.mission.data.local.LocalMission
import com.upsaclay.mission.data.local.MissionDao
import com.upsaclay.news.data.local.AnnouncementDao
import com.upsaclay.news.data.local.model.LocalAnnouncement

@Database(
    entities = [
        LocalAnnouncement::class,
        LocalConversation::class,
        LocalMessage::class,
        LocalConversationMessage::class,
        LocalNotificationMessage::class,
        LocalMission::class
    ],
    version = 3,
    exportSchema = false
)
abstract class GedoiseDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun conversationMessageDao(): ConversationMessageDao
    abstract fun notificationMessageDao(): NotificationMessageDao
    abstract fun missionDao(): MissionDao
}