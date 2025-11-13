package com.upsaclay.message.presentation.conversation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.common.utils.getElapsedTimeValue
import com.upsaclay.message.R
import com.upsaclay.message.domain.entity.Conversation.ConversationState
import com.upsaclay.message.domain.entity.ConversationUi
import com.upsaclay.message.domain.entity.Message.MessageState
import com.upsaclay.message.domain.messageFixture

@Composable
fun ConversationItem(
    modifier: Modifier = Modifier,
    conversationUi: ConversationUi,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val lastMessage = conversationUi.lastMessage
    val interlocutor = conversationUi.interlocutor
    val elapsedTimeValue = getElapsedTimeValue(lastMessage.date)
    val text = when(lastMessage.state) {
        MessageState.SENT, MessageState.DRAFT, MessageState.ERROR -> lastMessage.content
        MessageState.SENDING -> stringResource(R.string.sending)
    }
    val notSender = lastMessage.senderId == interlocutor.id

    SwitchConversationItem(
        modifier = modifier,
        interlocutor = conversationUi.interlocutor,
        conversationState = conversationUi.conversationState,
        text = text,
        unread = notSender && !lastMessage.seen,
        elapsedTime = elapsedTimeValue,
        onClick = onClick,
        onLongClick = onLongClick
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SwitchConversationItem(
    modifier: Modifier = Modifier,
    interlocutor: User,
    conversationState: ConversationState,
    unread: Boolean,
    text: String,
    elapsedTime: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val loading = conversationState == ConversationState.CREATING || conversationState == ConversationState.DELETING
    val interlocutorName = if (interlocutor.state != User.UserState.DELETED) {
        interlocutor.fullName
    } else {
        stringResource(id = com.upsaclay.common.R.string.deleted_user)
    }
    val fontWeight = if (unread) FontWeight.SemiBold else FontWeight.Normal
    val textColor = if (unread) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.previewText
    val alpha = if (loading) 0.5f else 1f

    ListItem(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .alpha(alpha),
        leadingContent = {
            ProfilePicture(
                url = interlocutor.profilePictureUrl,
                scale = 0.5f
            )
        },
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.smallSpacing()
            ) {
                Text(
                    modifier = Modifier.weight(1f, fill = false),
                    text = interlocutorName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = fontWeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = elapsedTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor,
                    fontWeight = fontWeight
                )
            }
        },
        supportingContent = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                fontWeight = fontWeight,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = if (unread) {
            {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error)
                        .size(10.dp)
                )
            }
        } else null
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun ReadConversationItemPreview() {
    GedoiseTheme {
        Surface {
            SwitchConversationItem(
                interlocutor = userFixture,
                conversationState = ConversationState.CREATED,
                unread = false,
                text = messageFixture.content,
                elapsedTime = "1 min",
                onClick = {},
                onLongClick = {}
            )
        }
    }
}

@PhonePreviews
@Composable
private fun UnreadConversationItemPreview() {
    GedoiseTheme {
        Surface {
            SwitchConversationItem(
                interlocutor = userFixture,
                conversationState = ConversationState.CREATED,
                unread = true,
                text = messageFixture.content,
                elapsedTime = "1 min",
                onClick = {},
                onLongClick = {}
            )
        }
    }
}

@PhonePreviews
@Composable
private fun SendingConversationItemPreview() {
    GedoiseTheme {
        Surface {
            SwitchConversationItem(
                interlocutor = userFixture,
                conversationState = ConversationState.CREATING,
                unread = false,
                text = messageFixture.content,
                elapsedTime = "1 min",
                onClick = {},
                onLongClick = {}
            )
        }
    }
}


