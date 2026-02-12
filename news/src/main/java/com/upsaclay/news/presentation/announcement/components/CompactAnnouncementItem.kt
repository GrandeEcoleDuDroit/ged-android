package com.upsaclay.news.presentation.announcement.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.upsaclay.common.extension.displayName
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.supportingText
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.common.utils.getElapsedTimeValue
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.Announcement.AnnouncementState
import com.upsaclay.news.domain.announcement.longAnnouncementFixture

@Composable
internal fun CompactAnnouncementItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    val elapsedTimeValue = getElapsedTimeValue(announcement.date)
    val alpha = if (announcement.state == AnnouncementState.PUBLISHING) 0.5f else 1f

    ListItem(
        modifier = modifier
            .clickable(onClick = onClick)
            .alpha(alpha),
        leadingContent = {
            LeadingContent(
                state = announcement.state,
                profilePictureUrl = announcement.author.profilePictureUrl
            )
        },
        headlineContent = {
            Row(
                horizontalArrangement = Arrangement.smallSpacing(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(fill = false, weight = 1f),
                    text = announcement.author.displayName(),
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = elapsedTimeValue,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.supportingText
                )
            }
        },
        supportingContent = {
            Text(
                text = announcement.title ?: announcement.content,
                color = MaterialTheme.colorScheme.supportingText,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            OptionButton(
                modifier = Modifier
                    .testTag(stringResource(id = R.string.announcement_option_button_tag)),
                contentDescription = stringResource(id = R.string.announcement_option_icon_description),
                onClick = onOptionClick
            )
        }
    )
}

@Composable
private fun LeadingContent(
    state: AnnouncementState,
    profilePictureUrl: String?
) {
    if (state == AnnouncementState.ERROR) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.smallSpacing()
        ) {
            Icon(
                painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )

            ProfilePicture(
                url = profilePictureUrl,
                scale = 0.5f
            )
        }
    } else {
        ProfilePicture(
            url = profilePictureUrl,
            scale = 0.5f
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
private fun CompactAnnouncementItemPreview() {
    GedoiseTheme {
        Surface {
            CompactAnnouncementItem(
                announcement = longAnnouncementFixture,
                onClick = {},
                onOptionClick = {}
            )
        }
    }
}

@PhonePreviews
@Composable
private fun PublishingCompactAnnouncementItemPreview() {
    GedoiseTheme {
        Surface {
            CompactAnnouncementItem(
                announcement = longAnnouncementFixture.copy(state = AnnouncementState.PUBLISHING),
                onClick = {},
                onOptionClick = {}
            )
        }
    }
}

@PhonePreviews
@Composable
private fun ErrorCompactAnnouncementItemPreview() {
    GedoiseTheme {
        Surface {
            CompactAnnouncementItem(
                announcement = longAnnouncementFixture.copy(state = AnnouncementState.ERROR),
                onClick = {},
                onOptionClick = {}
            )
        }
    }
}