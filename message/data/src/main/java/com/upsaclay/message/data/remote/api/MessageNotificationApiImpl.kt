package com.upsaclay.message.data.remote.api

import com.google.gson.Gson
import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.common.data.remote.api.FcmApi
import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.data.mapper.toFcm
import com.upsaclay.message.data.mapper.toRemote
import com.upsaclay.message.domain.entity.MessageNotification

class MessageNotificationApiImpl(private val fcmApi: FcmApi): MessageNotificationApi {
    private val gson = Gson()

    override suspend fun sendNotification(currentUser: User, messageNotification: MessageNotification) {
        mapServerResponseException(
            block = {
                val fcmMessage = messageNotification.toRemote(currentUser).toFcm()
                fcmApi.sendNotification(messageNotification.conversation.interlocutor.id, gson.toJson(fcmMessage))
            },
            message = "Failed to send fcm notification"
        )
    }
}