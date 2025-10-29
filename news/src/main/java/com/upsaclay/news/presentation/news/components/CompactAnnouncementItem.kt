package com.upsaclay.news.presentation.news.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.upsaclay.common.R
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.utils.Phones
import com.upsaclay.common.utils.getElapsedTimeValue
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.Announcement.AnnouncementState
import com.upsaclay.news.domain.longAnnouncementFixture

@Composable
internal fun CompactAnnouncementItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    val elapsedTimeValue = getElapsedTimeValue(announcement.date)
    when (announcement.state) {
        AnnouncementState.PUBLISHED, AnnouncementState.DRAFT -> {
            DefaultItem(
                modifier = modifier
                    .clickable(onClick = onClick)
                    .padding(
                        horizontal = dimensionResource(R.dimen.medium_padding),
                        vertical = dimensionResource(R.dimen.small_medium_padding)
                    ),
                announcement = announcement,
                elapsedTimeValue = elapsedTimeValue,
                onOptionClick = onOptionClick
            )
        }

        AnnouncementState.PUBLISHING -> {
            PublishingItem(
                modifier = modifier,
                announcement = announcement,
                elapsedTimeValue = elapsedTimeValue,
                onClick = onClick,
                onOptionClick = onOptionClick
            )
        }

        AnnouncementState.ERROR -> {
            ErrorItem(
                modifier = modifier,
                announcement = announcement,
                elapsedTimeValue = elapsedTimeValue,
                onClick = onClick,
                onOptionClick = onOptionClick
            )
        }
    }
}

@Composable
private fun DefaultItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    elapsedTimeValue: String,
    onOptionClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding))
    ) {
        ProfilePicture(
            url = announcement.author.profilePictureUrl,
            scale = 0.5f
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.very_extra_small_padding))
        ) {
            Row(
                horizontalArrangement = Arrangement.smallSpacing(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = announcement.author.fullName,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(fill = false, weight = 1f)
                )

                Text(
                    text = elapsedTimeValue,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.previewText
                )
            }

            Text(
                text = announcement.title ?: announcement.content,
                color = MaterialTheme.colorScheme.previewText,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        OptionButton(
            modifier = Modifier.testTag(stringResource(id = com.upsaclay.news.R.string.announcement_option_button_tag)),
            contentDescription = stringResource(id = com.upsaclay.news.R.string.announcement_option_icon_description),
            onClick = onOptionClick
        )
    }
}

@Composable
private fun PublishingItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    elapsedTimeValue: String,
    onClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    DefaultItem(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(
                horizontal = dimensionResource(R.dimen.medium_padding),
                vertical = dimensionResource(R.dimen.small_medium_padding)
            )
            .alpha(0.5f),
        announcement = announcement,
        elapsedTimeValue = elapsedTimeValue,
        onOptionClick = onOptionClick
    )
}

@Composable
private fun ErrorItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    elapsedTimeValue: String,
    onClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = dimensionResource(R.dimen.medium_padding),
                vertical = dimensionResource(R.dimen.small_medium_padding)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.small_padding))
    ) {
        Icon(
            painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_error),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )

        DefaultItem(
            modifier = Modifier.weight(1f),
            announcement = announcement,
            elapsedTimeValue = elapsedTimeValue,
            onOptionClick = onOptionClick
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
private fun DefaultItemPreview() {
    GedoiseTheme {
        Surface {
            DefaultItem(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.small_medium_padding))
                    .clickable(onClick = {}),
                announcement = longAnnouncementFixture,
                elapsedTimeValue = "1 min",
                onOptionClick = {}
            )
        }
    }
}

@Phones
@Composable
private fun PublishingItemPreview() {
    GedoiseTheme {
        Surface {
            PublishingItem(
                announcement = longAnnouncementFixture,
                elapsedTimeValue = "1 min",
                onClick = {},
                onOptionClick = {}
            )
        }
    }
}

@Phones
@Composable
private fun ErrorItemPreview() {
    GedoiseTheme {
        Surface {
            ErrorItem(
                announcement = longAnnouncementFixture.copy(state = AnnouncementState.ERROR),
                elapsedTimeValue = "1 min",
                onClick = {},
                onOptionClick = {}
            )
        }
    }
}