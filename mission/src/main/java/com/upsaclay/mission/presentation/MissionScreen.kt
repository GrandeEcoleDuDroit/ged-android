package com.upsaclay.mission.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.components.SimpleFloatingActionButton
import com.upsaclay.common.presentation.components.TitleTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.informationText
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun MissionDestination(
    onCreateMissionClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewMode: MissionViewModel = koinViewModel()
) {
    val uiState = viewMode.uiState.collectAsState()

    MissionScreen(
        memberUser = uiState.value.user?.isMember == true,
        onCreateMissionClick = onCreateMissionClick,
        bottomBar = bottomBar
    )
}

@Composable
private fun MissionScreen(
    memberUser: Boolean,
    onCreateMissionClick: () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TitleTopBar(title = stringResource(com.upsaclay.common.R.string.mission))
        },
        bottomBar = bottomBar,
        floatingActionButton = {
            if (memberUser) {
                SimpleFloatingActionButton(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    },
                    onClick = onCreateMissionClick
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_mission),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.informationText
                    )
                }
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
private fun MissionScreenPreview() {
    GedoiseTheme {
        MissionScreen(
            memberUser = true,
            onCreateMissionClick = {},
            bottomBar = {}
        )
    }
}