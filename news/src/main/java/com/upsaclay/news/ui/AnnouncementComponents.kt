package com.upsaclay.news.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.model.ElapsedTime
import com.upsaclay.common.domain.usecase.GetElapsedTimeUseCase
import com.upsaclay.common.ui.components.ProfilePicture
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing
import com.upsaclay.news.R
import com.upsaclay.news.announcementFixture
import com.upsaclay.news.domain.model.Announcement
import java.time.format.DateTimeFormatter

@Composable
internal fun ReadOnlyShortAnnouncementItem(
    announcement: Announcement,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val elapsedTime = GetElapsedTimeUseCase().fromLocalDateTime(announcement.date)
    val elapsedTimeValue = when(elapsedTime) {
        is ElapsedTime.Now -> stringResource(id = com.upsaclay.common.R.string.now)
        is ElapsedTime.Minute -> context.getString(com.upsaclay.common.R.string.minute_ago, elapsedTime.value)
        is ElapsedTime.Hour -> context.getString(com.upsaclay.common.R.string.hour_ago, elapsedTime.value)
        is ElapsedTime.Day -> context.getString(com.upsaclay.common.R.string.day_ago, elapsedTime.value)
        is ElapsedTime.Week -> context.getString(com.upsaclay.common.R.string.week_ago, elapsedTime.value)
        is ElapsedTime.After -> {
            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            announcement.date.format(dateFormat)
        }
    }

    Row(
        modifier = Modifier
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.smallMedium),
        verticalAlignment = Alignment.Top,
    ) {
        ProfilePicture(
            imageUrl = announcement.author.profilePictureUrl,
            scaleImage = 0.5f
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Column {
            Row {
                Text(
                    text = announcement.author.fullName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(fill = false, weight = 1f)
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                Text(
                    text = elapsedTimeValue,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                )
            }

            Text(
                text = announcement.title ?: announcement.content,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
internal fun EditableShortAnnouncementItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val context = LocalContext.current
    val elapsedTime = GetElapsedTimeUseCase().fromLocalDateTime(announcement.date)
    val elapsedTimeValue = when(elapsedTime) {
        is ElapsedTime.Now -> stringResource(id = com.upsaclay.common.R.string.now)
        is ElapsedTime.Minute -> context.getString(com.upsaclay.common.R.string.minute_ago, elapsedTime.value)
        is ElapsedTime.Hour -> context.getString(com.upsaclay.common.R.string.hour_ago, elapsedTime.value)
        is ElapsedTime.Day -> context.getString(com.upsaclay.common.R.string.day_ago, elapsedTime.value)
        is ElapsedTime.Week -> context.getString(com.upsaclay.common.R.string.week_ago, elapsedTime.value)
        is ElapsedTime.After -> {
            val dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            announcement.date.format(dateFormat)
        }
    }
    Row(
        modifier = modifier
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.spacing.smallMedium),
        verticalAlignment = Alignment.Top,
    ) {
        ProfilePicture(
            imageUrl = announcement.author.profilePictureUrl,
            scaleImage = 0.5f
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Column(modifier = Modifier.weight(1f)) {
            Row {
                Text(
                    text = announcement.author.fullName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(fill = false, weight = 1f)
                )

                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                Text(
                    text = elapsedTimeValue,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                )
            }

            Text(
                text = announcement.title ?: announcement.content,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.MoreVert,
                tint = Color.Gray,
                contentDescription = stringResource(id = R.string.announcement_item_more_vert_description)
            )
        }
    }
}

@Preview
@Composable
internal fun ReadOnlyShortAnnouncementItemPreview(){
    GedoiseTheme {
        ReadOnlyShortAnnouncementItem(
            announcement = announcementFixture,
            onClick = {}
        )
    }
}

@Preview
@Composable
internal fun EditableShortAnnouncementItemPreview(){
    GedoiseTheme {
        EditableShortAnnouncementItem(
            announcement = announcementFixture,
            onClick = {},
            onEditClick = {}
        )
    }
}