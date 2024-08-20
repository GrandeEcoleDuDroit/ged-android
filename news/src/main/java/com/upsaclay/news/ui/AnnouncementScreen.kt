package com.upsaclay.news.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.upsaclay.common.domain.model.ElapsedTime
import com.upsaclay.common.domain.usecase.GetElapsedTimeUseCase
import com.upsaclay.common.ui.components.ProfilePicture
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing
import com.upsaclay.news.announcementFixture
import com.upsaclay.news.domain.model.Announcement
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter

@Composable
fun AnnouncementScreen(
    modifier: Modifier = Modifier,
    newsViewModel: NewsViewModel = koinViewModel()
) {
    val announcement = newsViewModel.displayedAnnouncement

    Column(
        modifier = modifier.padding(
            start = MaterialTheme.spacing.medium,
            end = MaterialTheme.spacing.medium,
            bottom = MaterialTheme.spacing.medium
        )
    ) {

        TopSection(announcement)

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        announcement.title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(MaterialTheme.spacing.medium))
        }

        Text(
            text = announcement.content,
        )
    }
}

@Composable
fun TopSection(
    announcement: Announcement
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
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfilePicture(
            imageUrl = announcement.author.profilePictureUrl,
            scaleImage = 0.45f
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Text(
            text = announcement.author.fullName,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Text(
            text = elapsedTimeValue,
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AnnouncementScreenPreview(){
    GedoiseTheme {

        val announcement = announcementFixture

        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        ) {

            TopSection(announcement)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            announcement.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.height(MaterialTheme.spacing.medium))
            }

            Text(
                text = announcement.content,
            )
        }
    }
}