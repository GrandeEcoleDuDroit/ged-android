package com.upsaclay.news.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.ui.unit.LayoutDirection
import com.upsaclay.common.presentation.components.EmptyText
import com.upsaclay.common.presentation.components.SectionTitle
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.padding
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.Announcement.AnnouncementState
import com.upsaclay.news.domain.announcement.announcementsFixture
import com.upsaclay.news.presentation.announcement.components.CompactAnnouncementItem

@Composable
fun AnnouncementSection(
    modifier: Modifier = Modifier,
    announcements: List<Announcement>?,
    onSeeAllAnnouncementsClick: () -> Unit,
    onAnnouncementClick: (String) -> Unit,
    onUncreatedAnnouncementClick: (Announcement) -> Unit,
    onAnnouncementOptionClick: (Announcement) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.padding.medium)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SectionTitle(
                title = stringResource(id = R.string.announcements),
                modifier = Modifier.testTag(stringResource(id = R.string.news_screen_empty_announcement_text_tag))
            )

            TextButton(
                modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.extra_small_button_size)),
                contentPadding = PaddingValues(
                    start = ButtonDefaults.TextButtonContentPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = ButtonDefaults.TextButtonContentPadding.calculateEndPadding(LayoutDirection.Ltr)
                ),
                onClick = onSeeAllAnnouncementsClick
            ) {
                Text(text = stringResource(com.upsaclay.common.R.string.see_all))
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
                        EmptyText(
                            modifier = Modifier.padding(top = MaterialTheme.padding.small),
                            text = stringResource(id = R.string.no_announcement)
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
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun AnnouncementContentPreview() {
    GedoiseTheme {
        Surface {
            AnnouncementSection(
                announcements = announcementsFixture,
                onSeeAllAnnouncementsClick = {},
                onAnnouncementClick = {},
                onUncreatedAnnouncementClick = {},
                onAnnouncementOptionClick = {}
            )
        }
    }
}