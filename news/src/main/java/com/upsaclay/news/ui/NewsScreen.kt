package com.upsaclay.news.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.ui.components.PullToRefreshComponent
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing
import com.upsaclay.news.R
import com.upsaclay.news.announcementItemsFixture
import com.upsaclay.news.domain.model.Announcement
import org.koin.androidx.compose.koinViewModel

private const val URL_BLOGSPOT = "https://grandeecoledudroit.blogspot.com/"

@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    newsViewModel: NewsViewModel = koinViewModel()
) {
    val announcements = newsViewModel.announcements.collectAsState(emptyList())
    var isRefreshing by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isRefreshing) {
        newsViewModel.refreshAnnouncements()
        isRefreshing = false
    }

    PullToRefreshComponent(
        onRefresh = {
            isRefreshing = true
        }
    ) {
        Column(modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium)) {
            ShortRecentAnnouncementSection(
                announcements = announcements.value,
                modifier = modifier.padding(horizontal = MaterialTheme.spacing.medium)
            )

            Spacer(modifier = modifier.height(MaterialTheme.spacing.large))

            PostSection(modifier = modifier
                .padding(horizontal = MaterialTheme.spacing.medium)
            )
        }
    }
}

@Composable
private fun ShortRecentAnnouncementSection(
    announcements: List<Announcement>,
    modifier: Modifier = Modifier
){
    Text(
        text = stringResource(id = R.string.recent_announcement),
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )

    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

    LazyColumn {
        if (announcements.isEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.no_announcement),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
        else {
            items(announcements) { announcement ->
                ShortAnnouncementCard(
                    announcement = announcement
                )
            }
        }
    }
}

@Composable
private fun PostSection(modifier: Modifier = Modifier){
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = com.upsaclay.news.R.string.news_ged),
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ){
            Text(text = "POST SECTION")
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun NewsScreenPreview(){
    GedoiseTheme {
        PullToRefreshComponent(
            onRefresh = { },
        ) {
            Column(modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium)) {

                ShortRecentAnnouncementSection(
                    announcements = announcementItemsFixture,
                    Modifier.padding(horizontal = MaterialTheme.spacing.medium)
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                Column(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium)) {
                    Text(
                        text = stringResource(id = com.upsaclay.news.R.string.news_ged),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ){
                        Text(text = "POST SECTION")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun ShortRecentAnnouncementSectionPreview(){
    GedoiseTheme {
        Column {
            ShortRecentAnnouncementSection(
                announcements = announcementItemsFixture
            )
        }
    }
}