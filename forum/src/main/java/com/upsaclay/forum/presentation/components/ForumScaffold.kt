package com.upsaclay.forum.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.R
import com.upsaclay.common.presentation.components.TitleTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones

@Composable
fun ForumScaffold(
    onCreateMissionClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { TitleTopBar(title = stringResource(R.string.forum)) },
        bottomBar = bottomBar,
        floatingActionButton = { CreateMissionFAB(onClick = onCreateMissionClick) },
        content = content
    )
}

@Composable
private fun CreateMissionFAB(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = null
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
private fun ForumScaffoldPreview() {
    GedoiseTheme {
        Surface {
            ForumScaffold(
                onCreateMissionClick = {},
                bottomBar = {},
                content = {}
            )
        }
    }
}