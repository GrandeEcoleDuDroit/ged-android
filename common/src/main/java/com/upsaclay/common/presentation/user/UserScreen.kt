package com.upsaclay.common.presentation.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.mediumPadding
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones

@Composable
fun UserDestination(
    onBackClick: () -> Unit,
    user: User
) {
    UserScreen(
        onBackClick = onBackClick,
        user = user
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserScreen(
    onBackClick: () -> Unit,
    user: User
) {
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = user.fullName
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .mediumPadding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfilePicture(
                url = user.profilePictureUrl,
                scale = 1.8f
            )

            SelectionContainer {
                UserInformationItems(user = user)
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun UserScreenPreview() {
    GedoiseTheme {
        Surface {
            UserScreen(
                onBackClick = {},
                user = userFixture
            )
        }
    }
}