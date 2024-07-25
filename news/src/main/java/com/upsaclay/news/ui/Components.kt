package com.upsaclay.news.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.core.R
import com.upsaclay.core.ui.components.SmallShowButton
import com.upsaclay.core.ui.theme.GedoiseColor
import com.upsaclay.core.ui.theme.GedoiseTheme
import com.upsaclay.core.ui.theme.spacing
import com.upsaclay.news.data.model.Announcement

@Composable
internal fun ShortAnnouncementCard(
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
                painter = painterResource(id = R.drawable.ic_person),
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
                    text = announcement.author.fullName,
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
internal fun FullAnnouncementPopUp(
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
                painter = painterResource(id = R.drawable.ic_person),
                contentDescription = stringResource(id = com.upsaclay.news.R.string.announcement_author_picture_description),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(GedoiseColor.OnSurface)
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Column {
                Text(
                    text = announcement.author.fullName,
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
                text = stringResource(id = R.string.close),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun FullAnnouncementPopupPreview(){
    GedoiseTheme {
        FullAnnouncementPopUp(
            announcement = announcementFixture,
            modifier = Modifier.padding(MaterialTheme.spacing.medium),
            {}
        )
    }
}

@Preview
@Composable
internal fun ShortAnnouncementCardPreview(){
    GedoiseTheme {
        ShortAnnouncementCard(
            announcement = announcementFixture,
        )
    }
}