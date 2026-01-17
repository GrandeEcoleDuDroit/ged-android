package com.upsaclay.app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.upsaclay.message.data.local.dao.ConversationDao
import com.upsaclay.message.data.local.dao.ConversationMessageDao
import com.upsaclay.message.data.local.dao.MessageDao
import com.upsaclay.message.data.local.dao.MessageNotificationDao
import com.upsaclay.message.data.local.model.LocalConversation
import com.upsaclay.message.data.local.model.LocalConversationMessage
import com.upsaclay.message.data.local.model.LocalMessage
import com.upsaclay.message.data.local.model.LocalMessageNotification
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
        LocalMessageNotification::class,
        LocalMission::class
    ],
    version = 1,
    exportSchema = false
)
abstract class GedoiseDatabase : RoomDatabase() {
    abstract fun announcementDao(): AnnouncementDao
    abstract fun conversationDao(): ConversationDao
    abstract fun messageDao(): MessageDao
    abstract fun conversationMessageDao(): ConversationMessageDao
    abstract fun messageNotificationDao(): MessageNotificationDao
    abstract fun missionDao(): MissionDao
}