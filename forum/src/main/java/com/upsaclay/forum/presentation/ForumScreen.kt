package com.upsaclay.forum.presentation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.upsaclay.common.extension.mediumPadding
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.domain.entity.Mission
import com.upsaclay.forum.domain.missionsFixture
import com.upsaclay.forum.presentation.components.ForumScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForumDestination(
    bottomBar: @Composable () -> Unit,
    onMissionClick: (Mission) -> Unit,
    onCreateMissionClick: () -> Unit,
    viewModel: ForumViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ForumScreen(
        missions = uiState.missions,
        bottomBar = bottomBar,
        onMissionClick = onMissionClick,
        onCreateMissionClick = onCreateMissionClick
    )
}

@Composable
private fun ForumScreen(
    missions: List<Mission>,
    bottomBar: @Composable () -> Unit,
    onMissionClick: (Mission) -> Unit,
    onCreateMissionClick: () -> Unit
) {
    ForumScaffold(
        onCreateMissionClick = onCreateMissionClick,
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
                onMissionClick = {},
                onCreateMissionClick = {}
            )
        }
    }
}