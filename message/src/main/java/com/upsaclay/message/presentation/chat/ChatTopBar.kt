package com.upsaclay.message.presentation.chat

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.smallMediumSpacing
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    interlocutor: User,
    onBackClick: () -> Unit,
    onInterlocutorClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(onTap = { onInterlocutorClick() })
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.smallMediumSpacing()
            ) {
                ProfilePicture(
                    url = interlocutor.profilePictureUrl,
                    scale = 0.4f
                )

                Text(text = interlocutor.fullName, style = MaterialTheme.typography.titleMedium)
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = com.upsaclay.common.R.string.arrow_back_icon_description)
                )
            }
        }
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Composable
private fun ChatTopBarPreview() {
    GedoiseTheme {
        Column {
            ChatTopBar(
                interlocutor = userFixture,
                onBackClick = {},
                onInterlocutorClick = {}
            )
        }
    }
}