package com.upsaclay.news.presentation.news.components

import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.upsaclay.common.R
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTopBar(
    userProfilePictureUrl: String? = null,
    onProfilePictureClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {},
                enabled = false,
                modifier = Modifier.scale(1.2f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ged_logo),
                    contentDescription = stringResource(id = R.string.ged_logo_description),
                    modifier = Modifier.scale(0.85f)
                )
            }
        },
        actions = {
            IconButton(
                onClick = onProfilePictureClick
            ) {
                ProfilePicture(url = userProfilePictureUrl)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

/*
 =====================================================================
                            Preview
 =====================================================================
 */

@Phones
@Composable
private fun NewsTopBarPreview() {
    GedoiseTheme {
        Surface {
            NewsTopBar(
                userProfilePictureUrl = null,
                onProfilePictureClick = {}
            )
        }
    }
}