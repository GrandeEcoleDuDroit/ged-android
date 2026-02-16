package com.upsaclay.news.presentation.announcement.components

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
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallMediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.ExpandableText
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.Announcement.AnnouncementState
import com.upsaclay.news.domain.announcement.longAnnouncementFixture
import com.upsaclay.news.presentation.announcement.AnnouncementPresentationUtils.extendedAnnouncementItemContentStyle
import com.upsaclay.news.presentation.announcement.AnnouncementPresentationUtils.extendedAnnouncementItemTitleStyle
import com.upsaclay.news.presentation.announcement.readannouncement.AnnouncementHeader

@Composable
internal fun ExtendedAnnouncementItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onOptionClick: () -> Unit,
    onAuthorClick: () -> Unit
) {
    when (announcement.state) {
        AnnouncementState.PUBLISHED, AnnouncementState.DRAFT -> {
            DefaultItem(
                modifier = modifier,
                announcement = announcement,
                onOptionClick = onOptionClick,
                onAuthorClick = onAuthorClick
            )
        }

        AnnouncementState.PUBLISHING -> {
            PublishingItem(
                modifier = modifier,
                announcement = announcement,
                onOptionClick = onOptionClick,
                onAuthorClick = onAuthorClick
            )
        }

        AnnouncementState.ERROR -> {
            ErrorItem(
                modifier = modifier,
                announcement = announcement,
                onOptionClick = onOptionClick,
                onAuthorClick = onAuthorClick
            )
        }
    }
}

@Composable
private fun DefaultItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onOptionClick: () -> Unit,
    onAuthorClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallMediumSpacing()
    ) {
        Header(
            announcement = announcement,
            onOptionClick = onOptionClick,
            onAuthorClick = onAuthorClick
        )

        announcement.title?.let {
            Text(
                modifier = Modifier.testTag(stringResource(id = R.string.read_screen_announcement_title_tag)),
                text = it,
                style = extendedAnnouncementItemTitleStyle
            )
        }

        ExpandableText(
            modifier = Modifier.testTag(stringResource(id = R.string.read_screen_announcement_content_tag)),
            text = announcement.content,
            maxLines = 2,
            style = extendedAnnouncementItemContentStyle,
        )
    }
}

@Composable
private fun PublishingItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onOptionClick: () -> Unit,
    onAuthorClick: () -> Unit
) {
    DefaultItem(
        modifier = modifier.alpha(0.5f),
        announcement = announcement,
        onOptionClick = onOptionClick,
        onAuthorClick = onAuthorClick
    )
}

@Composable
private fun ErrorItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onOptionClick: () -> Unit,
    onAuthorClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.small_padding))
        ) {
            Icon(
                painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )

            Header(
                announcement = announcement,
                onOptionClick = onOptionClick,
                onAuthorClick = onAuthorClick
            )
        }

        announcement.title?.let {
            Text(
                modifier = Modifier.testTag(stringResource(id = R.string.read_screen_announcement_title_tag)),
                text = it,
                style = extendedAnnouncementItemTitleStyle
            )
        }

        ExpandableText(
            text = announcement.content,
            modifier = Modifier.testTag(stringResource(id = R.string.read_screen_announcement_content_tag)),
            style = extendedAnnouncementItemContentStyle
        )
    }
}

@Composable
private fun Header(
    announcement: Announcement,
    onOptionClick: () -> Unit,
    onAuthorClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.smallSpacing()
    ) {
        AnnouncementHeader(
            modifier = Modifier.weight(1f),
            announcement = announcement,
            onClick = onAuthorClick
        )

        OptionButton(
            modifier = Modifier.testTag(stringResource(id = R.string.announcement_option_button_tag)),
            contentDescription = stringResource(id = R.string.announcement_option_icon_description),
            onClick = onOptionClick
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
private fun DefaultItemPreview() {
    GedoiseTheme {
        Surface {
            DefaultItem(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                announcement = longAnnouncementFixture,
                onOptionClick = {},
                onAuthorClick = {}
            )
        }
    }
}

@PhonePreviews
@Composable
private fun PublishingItemPreview() {
    GedoiseTheme {
        Surface {
            PublishingItem(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                announcement = longAnnouncementFixture,
                onOptionClick = {},
                onAuthorClick = {}
            )
        }
    }
}

@PhonePreviews
@Composable
private fun ErrorItemPreview() {
    GedoiseTheme {
        Surface {
            ErrorItem(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                announcement = longAnnouncementFixture.copy(state = AnnouncementState.ERROR),
                onOptionClick = {},
                onAuthorClick = {}
            )
        }
    }
}

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