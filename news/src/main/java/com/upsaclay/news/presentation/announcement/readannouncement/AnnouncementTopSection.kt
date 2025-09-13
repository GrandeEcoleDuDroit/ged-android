package com.upsaclay.news.presentation.announcement.readannouncement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.utils.Phones
import com.upsaclay.common.utils.getElapsedTimeValue
import com.upsaclay.news.R
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.longAnnouncementFixture

@Composable
fun AnnouncementTopSection(
    user: User,
    announcement: Announcement,
    onEditClick: () -> Unit
) {
    if (user.isMember && announcement.author.id == user.id) {
        EditableAnnouncementHeader(
            announcement = announcement,
            onEditClick = onEditClick
        )
    } else {
        AnnouncementHeader(
            modifier = Modifier.fillMaxWidth(),
            announcement = announcement
        )
    }
}

@Composable
private fun EditableAnnouncementHeader(
    announcement: Announcement,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding))
    ) {
        AnnouncementHeader(
            announcement = announcement,
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .testTag(stringResource(id = R.string.read_screen_option_button_tag))
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

@Composable
private fun AnnouncementHeader(
    modifier: Modifier = Modifier,
    announcement: Announcement
) {
    val elapsedTimeValue = getElapsedTimeValue(announcement.date)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding))
    ) {
        ProfilePicture(
            url = announcement.author.profilePictureUrl,
            scale = 0.4f
        )

        Text(
            modifier = Modifier.weight(fill = false, weight = 1f),
            text = announcement.author.fullName,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = elapsedTimeValue,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.previewText
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun AnnouncementHeaderPreview() {
    GedoiseTheme {
        Surface {
            AnnouncementHeader(
                announcement = longAnnouncementFixture
            )
        }
    }
}

@Phones
@Composable
private fun EditableAnnouncementHeaderPreview() {
    GedoiseTheme {
        Surface {
            EditableAnnouncementHeader(
                announcement = longAnnouncementFixture,
                onEditClick = {}
            )
        }
    }
}