package com.upsaclay.forum.presentation

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.R
import com.upsaclay.common.extension.mediumPadding
import com.upsaclay.common.presentation.components.TitleTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.domain.entity.Mission
import com.upsaclay.forum.domain.missionsFixture
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForumDestination(
    bottomBar: @Composable () -> Unit,
    onMissionClick: (Mission) -> Unit,
    viewModel: ForumViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ForumScreen(
        missions = uiState.missions,
        bottomBar = bottomBar,
        onMissionClick = onMissionClick
    )
}

@Composable
fun ForumScreen(
    missions: List<Mission>,
    bottomBar: @Composable () -> Unit,
    onMissionClick: (Mission) -> Unit
) {
    Scaffold(
        topBar = { TitleTopBar(title = stringResource(R.string.forum)) },
        bottomBar = bottomBar
    ) { innerPadding ->
        MissionFeed(
            modifier = Modifier.mediumPadding(innerPadding),
            missions = missions,
            onClick = onMissionClick
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
private fun ForumScreenPreview() {
    GedoiseTheme {
        Surface {
            ForumScreen(
                missions = missionsFixture,
                bottomBar = {},
                onMissionClick = {}
            )
        }
    }
}