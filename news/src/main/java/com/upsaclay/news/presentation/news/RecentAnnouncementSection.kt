package com.upsaclay.news.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.informationText
import com.upsaclay.common.presentation.theme.sectionTitle
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcementsFixture
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.Announcement.AnnouncementState
import com.upsaclay.news.presentation.announcement.components.CompactAnnouncementItem

@Composable
fun RecentAnnouncementSection(
    modifier: Modifier = Modifier,
    announcements: List<Announcement>?,
    onAnnouncementClick: (String) -> Unit,
    onUncreatedAnnouncementClick: (Announcement) -> Unit,
    onSeeAllAnnouncementsClick: () -> Unit,
    onAnnouncementOptionClick: (Announcement) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.recent_announcements),
                style = MaterialTheme.typography.sectionTitle,
                modifier = Modifier.testTag(stringResource(id = R.string.news_screen_empty_announcement_text_tag))
            )

            TextButton(
                modifier = Modifier.height(30.dp),
                onClick = onSeeAllAnnouncementsClick,
                contentPadding = PaddingValues(
                    top = dimensionResource(com.upsaclay.common.R.dimen.default_padding),
                    bottom = dimensionResource(com.upsaclay.common.R.dimen.default_padding),
                    start = ButtonDefaults.TextButtonContentPadding.calculateLeftPadding(
                        LayoutDirection.Rtl
                    ),
                    end = ButtonDefaults.TextButtonContentPadding.calculateRightPadding(
                        LayoutDirection.Rtl
                    )
                )
            ) {
                Text(
                    text = stringResource(com.upsaclay.common.R.string.see_all)
                )
            }
        }

        announcements?.let {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (announcements.isEmpty()) {
                    item {
                        Spacer(
                            modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.small_padding))
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.no_announcement),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.informationText,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(announcements) { announcement ->
                        CompactAnnouncementItem(
                            modifier = Modifier.testTag(stringResource(R.string.news_screen_recent_announcements_tag)),
                            announcement = announcement,
                            onClick = {
                                if (announcement.state == AnnouncementState.PUBLISHED) {
                                    onAnnouncementClick(announcement.id)
                                } else {
                                    onUncreatedAnnouncementClick(announcement)
                                }
                            },
                            onOptionClick = { onAnnouncementOptionClick(announcement) }
                        )
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressBar()
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun RecentAnnouncementContentPreview() {
    GedoiseTheme {
        Surface {
            RecentAnnouncementSection(
                announcements = announcementsFixture,
                onAnnouncementClick = {},
                onUncreatedAnnouncementClick = {},
                onSeeAllAnnouncementsClick = {},
                onAnnouncementOptionClick = {}
            )
        }
    }
}