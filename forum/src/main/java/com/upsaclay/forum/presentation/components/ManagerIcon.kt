package com.upsaclay.forum.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones

@Composable
fun ManagerIcon(
    modifier: Modifier = Modifier,
    profilePictureUrl: String?,
    name: String,
    scale: Float = 1f
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallSpacing(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfilePicture(
            url = profilePictureUrl,
            scale = scale
        )

        Text(
            text = name,
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
private fun ManagerIconPreview() {
    GedoiseTheme {
        Surface {
            ManagerIcon(
                profilePictureUrl = null,
                name = "John Doe"
            )
        }
    }
}