package com.upsaclay.news.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.upsaclay.common.domain.model.ClickableMenuItemData
import com.upsaclay.common.domain.model.Screen
import com.upsaclay.common.ui.components.ClickableMenuItem
import com.upsaclay.common.ui.components.PullToRefreshComponent
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing
import com.upsaclay.news.R
import com.upsaclay.news.announcementItemsFixture
import com.upsaclay.news.domain.model.Announcement
import com.upsaclay.news.domain.model.AnnouncementState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel = koinViewModel(),
    navController: NavController
) {
    val announcements = newsViewModel.announcements.collectAsState(emptyList()).value
    val user = newsViewModel.user.collectAsState(null).value
    val state = newsViewModel.announcementState.collectAsState(initial = AnnouncementState.DEFAULT).value

    LaunchedEffect(Unit) {
        newsViewModel.refreshAnnouncements()
    }

    user?.let {
        PullToRefreshComponent(
            onRefresh = { newsViewModel.refreshAnnouncements() },
            refreshing = state == AnnouncementState.LOADING,
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

            if(user.isMember) {
                Box(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.medium)
                        .fillMaxSize()
                ) {
                    ExtendedFloatingActionButton(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        onClick = { navController.navigate(Screen.CREATE_ANNOUNCEMENT.route) },
                        icon = {
                            Icon(
                                Icons.Filled.Edit,
                                stringResource(id = R.string.new_announcement)
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.new_announcement)) },
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentAnnouncementSection(
    announcements: List<Announcement>,
    onClickAnnouncement: (Announcement) -> Unit,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val sortedAnnouncements = announcements.sortedByDescending { it.date }

    Column(modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium)) {
        Text(
            text = stringResource(id = R.string.recent_announcements),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        LazyColumn(
            modifier = Modifier.heightIn(max = screenHeight * 0.4f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (announcements.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                    Text(
                        text = stringResource(id = R.string.no_announcements),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(sortedAnnouncements) { announcement ->
                    ShortAnnouncementItem(
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

       //TODO : Implémenter la récupération des posts
    }
}

@Preview(showBackground = true ,widthDp = 360, heightDp = 640)
@Composable
fun NewsScreenPreview(){
    val isMember = true
    GedoiseTheme {
        PullToRefreshComponent(onRefresh = { }) {
            Column {
                RecentAnnouncementSectionPreview()
                PostSection()
            }

            if(isMember) {
                Box(
                    modifier = Modifier
                        .padding(MaterialTheme.spacing.medium)
                        .fillMaxSize()
                ) {
                    ExtendedFloatingActionButton(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        onClick = { },
                        icon = {
                            Icon(
                                Icons.Filled.Edit,
                                stringResource(id = R.string.new_announcement)
                            )
                        },
                        text = { Text(text = stringResource(id = R.string.new_announcement)) },
                    )
                }
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