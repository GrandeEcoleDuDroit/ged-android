package com.upsaclay.mission.presentation.components.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.displayName
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.gold
import com.upsaclay.common.utils.PhonePreviews

@Composable
fun MissionUserItem(
    modifier: Modifier = Modifier,
    user: User,
    imageScale: Float,
    showAdminIndicator: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    trailingContent: @Composable (() -> Unit)? = null
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            ProfilePicture(
                url = user.profilePictureUrl,
                scale = imageScale
            )
        },
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.smallSpacing()
            ) {
                Text(
                    modifier = Modifier.weight(1f, fill = false),
                    text = user.displayName(),
                    style = textStyle,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                if (user.admin && showAdminIndicator) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.gold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        trailingContent = trailingContent
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun MissionUserItemPreview() {
    GedoiseTheme {
        Surface {
            MissionUserItem(
                user = userFixture,
                imageScale = 0.5f
            )
        }
    }
}