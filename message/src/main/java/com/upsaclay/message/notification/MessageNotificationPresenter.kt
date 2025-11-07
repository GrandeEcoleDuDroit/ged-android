package com.upsaclay.message.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.IconCompat
import com.upsaclay.common.IntentHelper
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.message.domain.MessageNotificationUtils
import com.upsaclay.message.domain.converter.ConversationJsonParser
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.MessageNotificationUi
import java.io.InputStream

const val CONVERSATION_ID_EXTRA = "conversation_id_extra"

@SuppressLint("MissingPermission")
class MessageNotificationPresenter (
    private val context: Context,
    private val imageRepository: ImageRepository,
    private val intentHelper: IntentHelper
) {
    private val notificationManager = NotificationManagerCompat.from(context)

    fun start() {
        createNotificationChannel()
    }

    suspend fun showNotification(messageNotificationUi: MessageNotificationUi) {
        if (!notificationManager.areNotificationsEnabled()) {
            return
        }

        val messages = messageNotificationUi.messages
        val interlocutor = messageNotificationUi.conversation.interlocutor
        val intent = buildConversationIntent(messageNotificationUi.conversation)
        val userIcon = createUserIcon(interlocutor.profilePictureUrl)
        val user = buildPerson(interlocutor, userIcon)

        val notification = buildNotification(
            interlocutor = interlocutor,
            messages = messages,
            person = user,
            intent = intent
        )

        notificationManager.notify(messageNotificationUi.conversation.id.hashCode(), notification)
    }

    fun clearNotification(conversationId: String) {
        notificationManager.cancel(conversationId.hashCode())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            MessageNotificationUtils.CHANNEL_ID,
            "Message",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Message notification"
        }

        notificationManager.createNotificationChannel(channel)
    }

    private fun buildConversationIntent(conversation: Conversation): PendingIntent {
        val intent = intentHelper.getMainActivityIntent(context).apply {
            putExtra(CONVERSATION_ID_EXTRA, ConversationJsonParser.toJson(conversation))
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        return PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun buildPerson(interlocutor: User, icon: IconCompat): Person {
        return Person.Builder()
            .setIcon(icon)
            .setName(interlocutor.fullName)
            .build()
    }

    private suspend fun createUserIcon(profilePictureUrl: String?): IconCompat {
        return runCatching {
            profilePictureUrl?.let { imageRepository.getImage(it) }
        }.getOrNull()?.use {
            IconCompat.createWithBitmap(getCircledBitmap(it))
        } ?: IconCompat.createWithResource(context, R.drawable.default_profile_picture)
    }

    private fun getCircledBitmap(inputStream: InputStream): Bitmap {
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val output = createBitmap(bitmap.width, bitmap.height)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(),
            (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(),
            paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    private fun buildNotification(
        interlocutor: User,
        messages: List<MessageNotificationUi.Message>,
        person: Person,
        intent: PendingIntent
    ): Notification {
        val messageStyle = NotificationCompat
            .MessagingStyle(person)
            .setConversationTitle(interlocutor.fullName)
            .also {
                messages.forEach { message ->
                    it.addMessage(
                        message.text,
                        message.timestamp,
                        person
                    )
                }
            }


        val notificationBuilder = NotificationCompat.Builder(context, MessageNotificationUtils.CHANNEL_ID)
            .setContentTitle(interlocutor.fullName)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(context.getColor(R.color.icon_background_color))
            .setCategory(Notification.CATEGORY_MESSAGE)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .setStyle(messageStyle)

        return notificationBuilder.build()
    }
}