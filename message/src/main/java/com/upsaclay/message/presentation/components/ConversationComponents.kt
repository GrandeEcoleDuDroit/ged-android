package com.upsaclay.message.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.model.ElapsedTime
import com.upsaclay.common.domain.usecase.GetElapsedTimeUseCase
import com.upsaclay.common.domain.usecase.LocalDateTimeFormatterUseCase
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseColor
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.userFixture2
import com.upsaclay.message.R
import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.utils.messagesFixture

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConversationItem(
    modifier: Modifier = Modifier,
    conversation: Conversation,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val lastMessage = conversation.messages.lastOrNull()
    val getElapsedTimeUseCase = GetElapsedTimeUseCase()
    val localDateTimeFormatterUseCase = LocalDateTimeFormatterUseCase()

    val elapsedTimeValue: String = if(lastMessage != null) {
        val elapsedTime = getElapsedTimeUseCase.fromLocalDateTime(lastMessage.date)

        when(elapsedTime) {
            is ElapsedTime.Now -> stringResource(id = com.upsaclay.common.R.string.now)

            is ElapsedTime.Minute, is ElapsedTime.Hour ->
                localDateTimeFormatterUseCase.formatHourMinute(lastMessage.date)

            is ElapsedTime.Day -> {
                if(elapsedTime.value == 1L) {
                    stringResource(id = com.upsaclay.common.R.string.yesterday)
                } else {
                    localDateTimeFormatterUseCase.formatDayMonthYear(lastMessage.date)
                }
            }

            else -> localDateTimeFormatterUseCase.formatDayMonthYear(lastMessage.date)
        }
    } else ""

    val unreadMessage = lastMessage?.let {
        it.sender == conversation.interlocutor && !it.isRead
    } ?: false

    Row(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(MaterialTheme.spacing.smallMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
            imageUrl = conversation.interlocutor.profilePictureUrl,
            scaleImage = 0.5f
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Row(verticalAlignment = Alignment.Top) {
            if (unreadMessage) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = conversation.interlocutor.fullName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

                        Text(
                            text = elapsedTimeValue,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = lastMessage!!.text,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.error)
                                .size(10.dp)
                        )
                    }
                }

            } else {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = conversation.interlocutor.fullName,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

                        Text(
                            text = elapsedTimeValue,
                            style = MaterialTheme.typography.bodySmall,
                            color = GedoiseColor.PreviewText
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    lastMessage?.let {
                        Text(
                            text = lastMessage.text,
                            style = MaterialTheme.typography.bodyMedium,
                            color = GedoiseColor.PreviewText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } ?: run {
                        Text(
                            text = stringResource(id = R.string.tap_to_chat),
                            style = MaterialTheme.typography.bodyMedium,
                            color = GedoiseColor.PreviewText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(showBackground = true)
@Composable
private fun ReadConversationItemPreview() {
    val conversation = Conversation(
        id = "1",
        interlocutor = userFixture2,
        messages = messagesFixture,
    )
    GedoiseTheme {
        ConversationItem(
            modifier = Modifier.fillMaxWidth(),
            conversation = conversation,
            onClick = { },
            onLongClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UnreadConversationItemPreview() {
    val conversation = Conversation(
        id = "1",
        interlocutor = userFixture2,
        messages = messagesFixture,
    )
    GedoiseTheme {
        ConversationItem(
            modifier = Modifier.fillMaxWidth(),
            conversation = conversation,
            onClick = { },
            onLongClick = { }
        )
    }
}