package com.upsaclay.message.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.model.ElapsedTime
import com.upsaclay.common.domain.usecase.GetElapsedTimeUseCase
import com.upsaclay.common.domain.usecase.LocalDateTimeFormatterUseCase
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.userFixture2
import com.upsaclay.message.domain.model.ConversationItemData
import com.upsaclay.message.utils.messageFixture2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConversationItem(
    modifier: Modifier = Modifier,
    conversationItemData: ConversationItemData,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val getElapsedTimeUseCase = GetElapsedTimeUseCase()
    val localDateTimeFormatterUseCase = LocalDateTimeFormatterUseCase()
    val elapsedTime = getElapsedTimeUseCase.fromLocalDateTime(conversationItemData.lastMessage.date)

    val elapsedTimeValue: String = when(elapsedTime) {
        is ElapsedTime.Now -> stringResource(id = com.upsaclay.common.R.string.now)

        is ElapsedTime.Minute, is ElapsedTime.Hour ->
            localDateTimeFormatterUseCase.formatHourMinute(conversationItemData.lastMessage.date)

        is ElapsedTime.Day -> {
            if(elapsedTime.value == 1L) {
                stringResource(id = com.upsaclay.common.R.string.yesterday)
            } else {
                localDateTimeFormatterUseCase.formatDayMonthYear(conversationItemData.lastMessage.date)
            }
        }

        else -> localDateTimeFormatterUseCase.formatDayMonthYear(conversationItemData.lastMessage.date)
    }

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
            imageUrl = conversationItemData.interlocutor.profilePictureUrl,
            scaleImage = 0.5f
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Row(verticalAlignment = Alignment.Top) {
            if (conversationItemData.isRead) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = conversationItemData.interlocutor.fullName,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = conversationItemData.lastMessage.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = elapsedTimeValue,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            } else {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = conversationItemData.interlocutor.fullName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.size(MaterialTheme.spacing.smallMedium))

                        Text(
                            text = elapsedTimeValue,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = conversationItemData.lastMessage.text,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.size(MaterialTheme.spacing.smallMedium))

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.error)
                                .size(10.dp)
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
private fun ConversationItemPreview() {
    val conversationItemData = ConversationItemData(
        id = 1,
        interlocutor = userFixture2,
        lastMessage = messageFixture2,
        isRead = false
    )
    GedoiseTheme {
        ConversationItem(
            modifier = Modifier.fillMaxWidth(),
            conversationItemData = conversationItemData,
            onClick = { },
            onLongClick = { }
        )
    }
}