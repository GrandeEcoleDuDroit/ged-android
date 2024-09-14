package com.upsaclay.news.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.model.ElapsedTime
import com.upsaclay.common.domain.usecase.GetElapsedTimeUseCase
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.news.announcementFixture
import com.upsaclay.news.domain.model.Announcement
import java.time.format.DateTimeFormatter

@Composable
internal fun AnnouncementItem(
    announcement: Announcement,
    modifier: Modifier = Modifier
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
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
            imageUrl = announcement.author.profilePictureUrl,
            scaleImage = 0.45f
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Text(
            text = announcement.author.fullName,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(fill = false, weight = 1f)
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Text(
            text = elapsedTimeValue,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
internal fun AnnouncementItemWithTitle(
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = announcement.author.fullName,
                    style = MaterialTheme.typography.titleSmall,
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

            Spacer(modifier = Modifier.height(2.dp))

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

@Preview(showBackground = true)
@Composable
private fun AnnouncementItemPreview() {
    GedoiseTheme {
        AnnouncementItem(announcement = announcementFixture)
    }
}

@Preview(showBackground = true)
@Composable
private fun AnnouncementItemWithTitlePreview(){
    GedoiseTheme {
        AnnouncementItemWithTitle(
            announcement = announcementFixture,
            onClick = {}
        )
    }
}