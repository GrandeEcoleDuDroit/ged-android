package com.upsaclay.news.presentation.announcement.readannouncement

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import com.upsaclay.common.extension.displayName
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.supportingText
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.common.utils.getElapsedTimeValue
import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.longAnnouncementFixture

@Composable
fun AnnouncementHeader(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = { onClick() })
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding))
    ) {
        ProfilePicture(
            url = announcement.author.profilePictureUrl,
            scale = 0.4f
        )

        Text(
            modifier = Modifier.weight(fill = false, weight = 1f),
            text = announcement.author.displayName(),
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = getElapsedTimeValue(announcement.date),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.supportingText
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun AnnouncementHeaderPreview() {
    GedoiseTheme {
        Surface {
            AnnouncementHeader(
                announcement = longAnnouncementFixture,
                onClick = {}
            )
        }
    }
}