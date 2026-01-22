package com.upsaclay.message.presentation.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.displayName
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.supportingText
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
    conversationUi: ConversationUi
) {
    val text = when (conversationUi.lastMessage.state) {
        MessageState.SENDING -> stringResource(R.string.sending)
        MessageState.ERROR -> stringResource(R.string.message_failed_to_send_error)
        else -> conversationUi.lastMessage.content
    }

    SwitchConversationItem(
        modifier = modifier.padding(vertical = 2.dp),
        interlocutor = conversationUi.interlocutor,
        conversationState = conversationUi.state,
        text = text,
        unread = conversationUi.lastMessage.senderId == conversationUi.interlocutor.id && !conversationUi.lastMessage.seen,
        elapsedTimeText = getElapsedTimeValue(conversationUi.lastMessage.date)
    )
}

@Composable
private fun SwitchConversationItem(
    modifier: Modifier = Modifier,
    interlocutor: User,
    conversationState: ConversationState,
    unread: Boolean,
    text: String,
    elapsedTimeText: String
) {
    when (conversationState) {
        ConversationState.DRAFT, ConversationState.CREATING, ConversationState.DELETING -> {
            LoadingConversationItem(
                modifier = modifier,
                interlocutor = interlocutor,
                text = text,
                elapsedTimeText = elapsedTimeText
            )
        }

        ConversationState.CREATED -> {
            if (unread) {
                UnreadConversationItem(
                    modifier = modifier,
                    interlocutor = interlocutor,
                    text = text,
                    elapsedTimeText = elapsedTimeText
                )
            } else {
                DefaultConversationItem(
                    modifier = modifier,
                    interlocutor = interlocutor,
                    text = text,
                    elapsedTimeText = elapsedTimeText
                )
            }
        }

        ConversationState.ERROR -> {
            ErrorConversationItem(
                modifier = modifier,
                interlocutor = interlocutor,
                text = text,
                elapsedTimeText = elapsedTimeText
            )
        }
    }
}

@Composable
private fun DefaultConversationItem(
    modifier: Modifier = Modifier,
    interlocutor: User,
    text: String,
    elapsedTimeText: String
) {
    ListItem(
        modifier = modifier,
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
                    text = interlocutor.displayName(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = elapsedTimeText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.supportingText
                )
            }
        },
        supportingContent = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.supportingText,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

@Composable
private fun UnreadConversationItem(
    modifier: Modifier = Modifier,
    interlocutor: User,
    text: String,
    elapsedTimeText: String
) {
    ListItem(
        modifier = modifier,
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
                    text = interlocutor.displayName(),
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = elapsedTimeText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        supportingContent = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
                    .size(10.dp)
            )
        }
    )
}

@Composable
private fun LoadingConversationItem(
    modifier: Modifier = Modifier,
    interlocutor: User,
    text: String,
    elapsedTimeText: String
) {
    DefaultConversationItem(
        modifier = modifier.alpha(0.5f),
        interlocutor = interlocutor,
        text = text,
        elapsedTimeText = elapsedTimeText
    )
}

@Composable
private fun ErrorConversationItem(
    modifier: Modifier = Modifier,
    interlocutor: User,
    text: String,
    elapsedTimeText: String
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.smallSpacing()
            ) {
                Icon(
                    painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_error),
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null
                )

                ProfilePicture(
                    url = interlocutor.profilePictureUrl,
                    scale = 0.5f
                )
            }
        },
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.smallSpacing()
            ) {
                Text(
                    modifier = Modifier.weight(1f, fill = false),
                    text = interlocutor.displayName(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = elapsedTimeText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.supportingText
                )
            }
        },
        supportingContent = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.supportingText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun DefaultConversationItemPreview() {
    GedoiseTheme {
        Surface {
            DefaultConversationItem(
                interlocutor = userFixture,
                text = messageFixture.content,
                elapsedTimeText = "1 min"
            )
        }
    }
}

@PhonePreviews
@Composable
private fun UnreadConversationItemPreview() {
    GedoiseTheme {
        Surface {
            UnreadConversationItem(
                interlocutor = userFixture,
                text = messageFixture.content,
                elapsedTimeText = "1 min"
            )
        }
    }
}

@PhonePreviews
@Composable
private fun LoadingConversationItemPreview() {
    GedoiseTheme {
        Surface {
            LoadingConversationItem(
                interlocutor = userFixture,
                text = messageFixture.content,
                elapsedTimeText = "1 min"
            )
        }
    }
}

@PhonePreviews
@Composable
private fun ErrorConversationItemPreview() {
    GedoiseTheme {
        Surface {
            ErrorConversationItem(
                interlocutor = userFixture,
                text = messageFixture.content,
                elapsedTimeText = "1 min"
            )
        }
    }
}