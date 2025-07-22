package com.upsaclay.forum.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones

@Composable
fun ManagerItem(
    modifier: Modifier = Modifier,
    user: User,
    imageScale: Float
) {
    Column(
        modifier = modifier.widthIn(max = 100.dp),
        verticalArrangement = Arrangement.smallSpacing(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfilePicture(
            url = user.profilePictureUrl,
            scale = imageScale
        )

        Text(
            text = user.fullName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun ManagerItemPreview() {
    GedoiseTheme {
        Surface {
            LazyRow(
                horizontalArrangement = Arrangement.mediumSpacing()
            ) {
                items(usersFixture) {
                    ManagerItem(
                        user = it,
                        imageScale = 0.5f
                    )
                }
            }
        }
    }
}