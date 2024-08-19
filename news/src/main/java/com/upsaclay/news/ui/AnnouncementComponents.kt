package com.upsaclay.news.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.ui.components.ProfilePicture
import com.upsaclay.common.ui.components.SmallShowButton
import com.upsaclay.common.ui.theme.GedoiseTheme
import com.upsaclay.common.ui.theme.spacing
import com.upsaclay.news.announcementFixture
import com.upsaclay.news.domain.model.Announcement

@Composable
internal fun ShortAnnouncementItem(
    announcement: Announcement,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.background)
            .padding(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfilePicture(
            imageUrl = announcement.author.profilePictureUrl,
            scaleImage = 0.4f
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = announcement.author.fullName,
                style = MaterialTheme.typography.labelLarge,
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

            Text(
                text = announcement.title,
                color = Color.Gray,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        SmallShowButton(onClick = onClick)
    }
}

@Preview
@Composable
internal fun ShortAnnouncementItemPreview(){
    GedoiseTheme {
        ShortAnnouncementItem(
            announcement = announcementFixture,
            onClick = {}
        )
    }
}