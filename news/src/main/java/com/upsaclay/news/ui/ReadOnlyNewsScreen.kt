package com.upsaclay.news.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.upsaclay.common.data.model.Screen
import com.upsaclay.common.ui.components.PullToRefreshComponent
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing
import com.upsaclay.news.R
import com.upsaclay.news.announcementItemsFixture
import com.upsaclay.news.domain.model.Announcement
import org.koin.androidx.compose.koinViewModel


private const val URL_BLOGSPOT = "https://grandeecoledudroit.blogspot.com/"

@Composable
fun ReadOnlyNewsScreen(
    newsViewModel: NewsViewModel = koinViewModel(),
    navController: NavController
) {
    val announcements = newsViewModel.announcements.collectAsState(emptyList()).value
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(isRefreshing) {
        newsViewModel.refreshAnnouncements()
        isRefreshing = false
    }

    PullToRefreshComponent(
        onRefresh = { isRefreshing = true }
    ) {
        Column {
            RecentAnnouncementSection(
                announcements = announcements,
                onClickAnnouncement = { announcement ->
                    newsViewModel.updateDisplayedAnnouncement(announcement)
                    navController.navigate(Screen.READ_ANNOUNCEMENT.route)
                }
            )

            PostSection()
        }
    }
}

@Composable
private fun RecentAnnouncementSection(
    announcements: List<Announcement>,
    onClickAnnouncement: (Announcement) -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium)) {
        Text(
            text = stringResource(id = R.string.recent_announcements),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.smallMedium))

        LazyColumn(Modifier.heightIn(max = screenHeight * 0.3f)) {
            if (announcements.isEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.no_announcements),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(announcements) { announcement ->
                    ReadOnlyShortAnnouncementItem(
                        announcement = announcement,
                        onClick = { onClickAnnouncement(announcement) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PostSection() {
    Column {
        Text(
            text = stringResource(id = R.string.news_ged),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
        )

       //TODO : Implémenter la récupération de posts
    }
}

@Preview(showBackground = true ,widthDp = 360, heightDp = 640)
@Composable
fun ReadOnlyNewsScreenPreview(){
    GedoiseTheme {
        PullToRefreshComponent(
            onRefresh = { },
        ) {
            Column {
                RecentAnnouncementSectionPreview()

                PostSection()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecentAnnouncementSectionPreview(){
    GedoiseTheme {
        Column {
            RecentAnnouncementSection(
                announcements = announcementItemsFixture,
                onClickAnnouncement = {},
            )
        }
    }
}