package com.upsaclay.message.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.upsaclay.message.data.local.model.LocalMessageNotification
import com.upsaclay.message.data.local.model.MESSAGE_NOTIFICATION_TABLE_NAME
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_CONVERSATION_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_TIMESTAMP

@Dao
interface MessageNotificationDao {
    @Query("""
        SELECT * 
        FROM $MESSAGE_NOTIFICATION_TABLE_NAME 
        WHERE $MESSAGE_CONVERSATION_ID = :conversationId
        ORDER BY $MESSAGE_TIMESTAMP
        """
    )
    suspend fun getMessageNotifications(conversationId: String): List<LocalMessageNotification>

    @Insert
    suspend fun insertMessageNotification(localMessageNotification: LocalMessageNotification)

    @Query("DELETE FROM $MESSAGE_NOTIFICATION_TABLE_NAME WHERE $MESSAGE_CONVERSATION_ID = :conversationId")
    suspend fun deleteMessageNotifications(conversationId: String)
}