package com.upsaclay.news.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upsaclay.core.data.User
import com.upsaclay.core.ui.components.SmallShowButton
import com.upsaclay.core.ui.components.StandardWebView
import com.upsaclay.core.ui.theme.GedoiseColor
import com.upsaclay.core.ui.theme.GedoiseTheme
import com.upsaclay.news.R
import com.upsaclay.news.data.model.Announcement
import java.time.LocalDateTime
import com.upsaclay.core.R as CoreResource

private const val URL_BLOGSPOT = "https://grandeecoledudroit.blogspot.com/"
@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel = viewModel()
){
    val news = newsViewModel.announcements.collectAsState()
    Column {
        ShortRecentAnnouncementSection(
            announcements = news.value
        )
        Spacer(modifier = Modifier.height(10.dp))
        NewsSection()
    }
}

@Composable
private fun ShortRecentAnnouncementSection(
    announcements: List<Announcement>,
    modifier: Modifier = Modifier
){
    Text(
        text = stringResource(id = R.string.recent_announcement),
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(10.dp))
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            if (announcements.isEmpty()){
                Text(
                    text = stringResource(id = R.string.no_announcement),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            else {
                announcements.forEach {
                    ShortAnnouncementCard(
                        announcement = it
                    )
                }
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
            .background(GedoiseColor.White)
    ) {
        Row(
            modifier = Modifier
                .background(GedoiseColor.White)
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
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = announcement.title,
                    color = Color.Gray,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            SmallShowButton(
                modifier = Modifier
                    .size(width = 50.dp, height = 25.dp),
                textSize = 9
            ) {

            }
        }
    }
}

@Composable
private fun FullAnnouncementPopUp(
    announcement: Announcement,
    onclick: () -> Unit
) {
    val announcementDate = "${announcement.date.dayOfMonth}/${announcement.date.monthValue}/${announcement.date.year}"
    Column(Modifier.padding(10.dp)) {
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
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = announcement.author.fullname,
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = "Edité le $announcementDate",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
        Text(
            text = announcement.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        Text(text = announcement.content)
        Spacer(Modifier.height(10.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onclick
        ) {
            Text(
                text = stringResource(id = CoreResource.string.close),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun NewsSection(){
    Column {
        Text(
            text = stringResource(id = com.upsaclay.news.R.string.news_ged),
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ){
            StandardWebView(
                url = URL_BLOGSPOT
            )
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
    announcementFixture,
    announcementFixture
)

@Preview
@Composable
private fun ShortRecentAnnouncementSectionPreview(){
    GedoiseTheme {
        ShortRecentAnnouncementSection(
            announcements = announcementItemsFixture
        )
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
            {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsSectionPreview(){
    GedoiseTheme {
        NewsSection()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun NewsScreenPreview(){
    GedoiseTheme {
        NewsScreen()
    }
}