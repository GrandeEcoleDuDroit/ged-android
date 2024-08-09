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
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.ui.components.PullToRefreshComponent
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing
import com.upsaclay.news.R
import com.upsaclay.news.data.model.Announcement
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime

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
        newsViewModel.updateAnnouncements()
        isRefreshing = false
    }

    PullToRefreshComponent(
        onRefresh = {
            isRefreshing = true
        }
    ) {
        Column {
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
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
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

internal val announcementFixture = Announcement(
    id = 1,
    title = "Rappel : Visite de cabinet le 23/03.",
    date = LocalDateTime.now(),
    content = "Nous vous informons que la visite de votre " +
            "cabinet médical est programmée pour le 23 mars. " +
            "Cette visite a pour but de s'assurer que toutes les normes de sécurité " +
            "et de conformité sont respectées, ainsi que de vérifier l'état général " +
            "des installations et des équipements médicaux." +
            "Nous vous recommandons de préparer tous les documents nécessaires et " +
            "de veiller à ce que votre personnel soit disponible pour répondre " +
            "à d'éventuelles questions ou fournir des informations supplémentaires. " +
            "Une préparation adéquate permettra de garantir que la visite se déroule " +
            "sans heurts et de manière efficace. N'hésitez pas à nous contacter si " +
            "vous avez des questions ou si vous avez besoin de plus amples informations" +
            " avant la date prévue",
    author = User(
        id = 1,
        firstName = "Patrick",
        lastName = "Dupont",
        email = "patrick.dupont@example.com",
        schoolLevel = "GED 1",
        isMember = false,
        profilePictureUrl = "https://i-mom.unimedias.fr/2020/09/16/dragon-ball-songoku.jpg?auto=format,compress&cs=tinysrgb&w=1200"
    )
)

internal val announcementItemsFixture = listOf(
    announcementFixture,
    announcementFixture
)