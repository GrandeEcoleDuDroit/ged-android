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
import androidx.compose.ui.platform.LocalContext
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
import com.upsaclay.news.announcementFixture
import com.upsaclay.news.domain.model.Announcement

@Composable
internal fun AnnouncementItem(
    modifier: Modifier = Modifier,
    announcement: Announcement
) {
    val context = LocalContext.current
    val localDateTimeFormatterUseCase = LocalDateTimeFormatterUseCase()
    val getElapsedTimeUseCase = GetElapsedTimeUseCase()

    val elapsedTime = getElapsedTimeUseCase.fromLocalDateTime(announcement.date)

    val elapsedTimeValue: String = when(elapsedTime) {
        is ElapsedTime.Now ->  context.getString(com.upsaclay.common.R.string.second_ago, elapsedTime.value)

        is ElapsedTime.Minute -> context.getString(com.upsaclay.common.R.string.minute_ago, elapsedTime.value)

        is ElapsedTime.Hour -> context.getString(com.upsaclay.common.R.string.hour_ago, elapsedTime.value)

        is ElapsedTime.Day -> context.getString(com.upsaclay.common.R.string.day_ago, elapsedTime.value)

        is ElapsedTime.Week -> context.getString(com.upsaclay.common.R.string.week_ago, elapsedTime.value)

        is ElapsedTime.Later -> localDateTimeFormatterUseCase.formatDayMonthYear(elapsedTime.value)
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
            color = GedoiseColor.PreviewText
        )
    }
}

@Composable
internal fun AnnouncementItemWithTitle(
    announcement: Announcement,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val getElapsedTimeUseCase = GetElapsedTimeUseCase()
    val localDateTimeFormatterUseCase = LocalDateTimeFormatterUseCase()
    val elapsedTime = getElapsedTimeUseCase.fromLocalDateTime(announcement.date)

    val elapsedTimeValue = when(elapsedTime) {
        is ElapsedTime.Now ->  context.getString(com.upsaclay.common.R.string.second_ago, elapsedTime.value)

        is ElapsedTime.Minute -> context.getString(com.upsaclay.common.R.string.minute_ago, elapsedTime.value)

        is ElapsedTime.Hour -> context.getString(com.upsaclay.common.R.string.hour_ago, elapsedTime.value)

        is ElapsedTime.Day -> context.getString(com.upsaclay.common.R.string.day_ago, elapsedTime.value)

        is ElapsedTime.Week -> context.getString(com.upsaclay.common.R.string.week_ago, elapsedTime.value)

        is ElapsedTime.Later -> localDateTimeFormatterUseCase.formatDayMonthYear(elapsedTime.value)
    }

    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
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
                    color = GedoiseColor.PreviewText,
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = announcement.title ?: announcement.content,
                color = GedoiseColor.PreviewText,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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
            onClick = { }
        )
    }
}