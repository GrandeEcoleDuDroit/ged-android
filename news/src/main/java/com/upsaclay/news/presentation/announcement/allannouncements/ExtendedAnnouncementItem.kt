package com.upsaclay.news.presentation.announcement.allannouncements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.domain.longAnnouncementFixture
import com.upsaclay.news.presentation.announcement.readannouncement.AnnouncementHeader

@Composable
internal fun ExtendedAnnouncementItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onClick: () -> Unit,
    onOptionClick: () -> Unit,
    onResendAnnouncementClick: () -> Unit
) {
    when (announcement.state) {
        AnnouncementState.PUBLISHED, AnnouncementState.DRAFT -> {
            DefaultItem(
                modifier = modifier
                    .clickable(onClick = onClick)
                    .padding(dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding)),
                announcement = announcement,
                onOptionClick = onOptionClick
            )
        }

        AnnouncementState.PUBLISHING -> {
            PublishingItem(
                modifier = modifier.clickable(onClick = onClick),
                announcement = announcement,
                onOptionClick = onOptionClick
            )
        }

        AnnouncementState.ERROR -> {
            ErrorItem(
                modifier = modifier.clickable(onClick = onClick),
                announcement = announcement,
                onResendAnnouncementClick = onResendAnnouncementClick,
                onOptionClick = onOptionClick
            )
        }
    }
}

@Composable
private fun DefaultItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onOptionClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        AnnouncementHeader(
            announcement = announcement,
            onOptionClick = onOptionClick
        )

        announcement.title?.let {
            Text(
                modifier = Modifier.testTag(stringResource(id = R.string.read_screen_announcement_title_tag)),
                text = it,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Text(
            modifier = Modifier.testTag(stringResource(id = R.string.read_screen_announcement_content_tag)),
            text = announcement.content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun PublishingItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onOptionClick: () -> Unit
) {
    DefaultItem(
        modifier = modifier
            .padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
            .alpha(0.5f),
        announcement = announcement,
        onOptionClick = onOptionClick
    )
}

@Composable
private fun ErrorItem(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onResendAnnouncementClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
    ) {
        TextButton(
            modifier = Modifier.align(Alignment.End),
            onClick = onResendAnnouncementClick
        ) {
            Row(
                horizontalArrangement = Arrangement.smallSpacing()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_outline_sync),
                    contentDescription = null
                )

                Text(
                    text = stringResource(com.upsaclay.common.R.string.resend)
                )
            }
        }

        DefaultItem(
            modifier = Modifier.weight(1f),
            announcement = announcement,
            onOptionClick = onOptionClick
        )
    }
}


/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Composable
private fun DefaultItemPreview() {
    GedoiseTheme {
        Surface {
            DefaultItem(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding)),
                announcement = longAnnouncementFixture,
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
                onResendAnnouncementClick = {},
                onOptionClick = {}
            )
        }
    }
}