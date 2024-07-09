package com.upsaclay.news.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.core.data.User
import com.upsaclay.core.ui.components.PullToRefreshComponent
import com.upsaclay.core.ui.components.SmallShowButton
import com.upsaclay.core.ui.components.StandardWebView
import com.upsaclay.core.ui.theme.GedoiseColor
import com.upsaclay.core.ui.theme.GedoiseTheme
import com.upsaclay.core.ui.theme.spacing
import com.upsaclay.news.R
import com.upsaclay.news.data.model.Announcement
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import com.upsaclay.core.R as CoreResource

private const val URL_BLOGSPOT = "https://grandeecoledudroit.blogspot.com/"
@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    newsViewModel: NewsViewModel = koinViewModel()
) {
    val announcements = newsViewModel.announcements.collectAsState(emptyList())
    var isRefreshing by remember {
        mutableStateOf(true)
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
            Spacer(modifier = modifier.height(MaterialTheme.spacing.medium))
            NewsSection(modifier = modifier
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
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
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

@Composable private fun ShortAnnouncementCard(
    modifier: Modifier = Modifier,
    announcement: Announcement,
){
    Card(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = com.upsaclay.core.R.drawable.ic_person),
                contentDescription = stringResource(id = com.upsaclay.news.R.string.announcement_author_picture_description),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(GedoiseColor.OnSurface)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = announcement.author.fullname,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = announcement.title,
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            SmallShowButton {

            }
        }
    }
}

@Composable
private fun FullAnnouncementPopUp(
    announcement: Announcement,
    modifier: Modifier = Modifier,
    onclick: () -> Unit
) {
    val announcementDate = "${announcement.date.dayOfMonth}/${announcement.date.monthValue}/${announcement.date.year}"
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = com.upsaclay.core.R.drawable.ic_person),
                contentDescription = stringResource(id = com.upsaclay.news.R.string.announcement_author_picture_description),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(GedoiseColor.OnSurface)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Column {
                Text(
                    text = announcement.author.fullname,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Edité le $announcementDate",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Text(
            text = announcement.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.extraSmall)
        )

        Spacer(Modifier.height(MaterialTheme.spacing.small))

        Text(text = announcement.content)

        Spacer(Modifier.height(MaterialTheme.spacing.small))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onclick
        ) {
            Text(
                text = stringResource(id = CoreResource.string.close),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun NewsSection(modifier: Modifier = Modifier){
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = com.upsaclay.news.R.string.news_ged),
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ){
            StandardWebView(
                url = URL_BLOGSPOT,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShortRecentAnnouncementSectionPreview(){
    GedoiseTheme {
        Column {
            ShortRecentAnnouncementSection(
                announcements = announcementItemsFixture
            )
        }
    }
}

@Preview
@Composable
private fun ShortAnnouncementCardPreview(){
    GedoiseTheme {
        ShortAnnouncementCard(
            announcement = announcementFixture,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FullAnnouncementScreenPreview(){
    GedoiseTheme {
        FullAnnouncementPopUp(
            announcement = announcementFixture,
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            {}
        )
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
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ){
                        Text(text = "WEB VIEW")
                    }
                }
            }
        }
    }
}

private val announcementFixture = Announcement(
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
        email = "patrick.dupont@example.com"
    )
)

private val announcementItemsFixture = listOf(
    announcementFixture,
    announcementFixture,

)