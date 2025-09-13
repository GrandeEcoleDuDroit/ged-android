package com.upsaclay.message.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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
import com.upsaclay.common.R
import com.upsaclay.common.domain.IntentHelper
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.message.domain.NotificationMessageUtils
import com.upsaclay.message.domain.converter.ConversationJsonConverter
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.NotificationMessage
import com.upsaclay.message.domain.entity.NotificationMessages

const val CONVERSATION_ID_EXTRA = "conversation_id_extra"

@SuppressLint("MissingPermission")
class NotificationMessagePresenter (
    private val context: Context,
    private val imageRepository: ImageRepository,
    private val intentHelper: IntentHelper
) {
    private val notificationManager = NotificationManagerCompat.from(context)

    fun start() {
        createNotificationChannel()
    }

    suspend fun showNotification(notificationMessages: NotificationMessages) {
        if (!notificationManager.areNotificationsEnabled()) {
            return
        }

        val messages = notificationMessages.messages
        val interlocutor = notificationMessages.conversation.interlocutor
        val intent = buildConversationIntent(notificationMessages.conversation)
        val userIcon = createUserIcon(interlocutor.profilePictureUrl)
        val user = buildPerson(interlocutor, userIcon)

        val notification = buildNotification(
            interlocutor = interlocutor,
            messages = messages,
            person = user,
            intent = intent
        )

        notificationManager.notify(notificationMessages.conversation.id.hashCode(), notification)
    }

    fun clearNotification(conversationId: String) {
        notificationManager.cancel(conversationId.hashCode())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NotificationMessageUtils.CHANNEL_ID,
            "Message",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Message notification"
        }

        notificationManager.createNotificationChannel(channel)
    }

    private fun buildConversationIntent(conversation: Conversation): PendingIntent {
        val intent = intentHelper.getMainActivityIntent(context).apply {
            putExtra(CONVERSATION_ID_EXTRA, ConversationJsonConverter.toConversationJson(conversation))
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
        val profilePicture = runCatching {
            profilePictureUrl?.let { imageRepository.getImage(it) }
        }.getOrNull()

        return profilePicture?.let {
            IconCompat.createWithBitmap(getCircledBitmap(it))
        } ?: IconCompat.createWithResource(context, R.drawable.default_profile_picture)
    }

    private fun getCircledBitmap(bitmap: Bitmap): Bitmap {
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
        messages: List<NotificationMessage.MessageContent>,
        person: Person,
        intent: PendingIntent
    ): Notification {
        val messageStyle = NotificationCompat
            .MessagingStyle(person)
            .setConversationTitle(interlocutor.fullName)
            .also {
                messages.forEach { message ->
                    it.addMessage(
                        message.content,
                        message.date,
                        person
                    )
                }
            }


        val notificationBuilder = NotificationCompat.Builder(context, NotificationMessageUtils.CHANNEL_ID)
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