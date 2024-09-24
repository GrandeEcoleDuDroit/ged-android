package com.upsaclay.message.data.remote

import com.google.firebase.firestore.PropertyName

data class RemoteMessage(
    @get:PropertyName("message_id")
    @set:PropertyName("message_id")
    var messageId: String = "",

    @get:PropertyName("sender_id")
    @set:PropertyName("sender_id")
    var senderId: String = "",

    @get:PropertyName("text")
    @set:PropertyName("text")
    var text: String = "",

    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: Long = 0,

    @get:PropertyName("is_read")
    @set:PropertyName("is_read")
    var isRead: Boolean = false,

    @get:PropertyName("type")
    @set:PropertyName("type")
    var type: String
)
