package com.upsaclay.message.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.upsaclay.message.data.local.model.LocalNotificationMessage
import com.upsaclay.message.data.model.MessageField.CONVERSATION_ID
import com.upsaclay.message.data.model.NOTIFICATION_MESSAGE_TABLE_NAME

@Dao
interface NotificationMessageDao {
    @Query("SELECT * FROM $NOTIFICATION_MESSAGE_TABLE_NAME WHERE $CONVERSATION_ID = :conversationId")
    suspend fun getNotificationMessages(conversationId: String): List<LocalNotificationMessage>

    @Insert
    suspend fun insertNotificationMessage(notificationMessage: LocalNotificationMessage)

    @Query("DELETE FROM $NOTIFICATION_MESSAGE_TABLE_NAME WHERE $CONVERSATION_ID = :conversationId")
    suspend fun deleteNotificationMessages(conversationId: String)
}