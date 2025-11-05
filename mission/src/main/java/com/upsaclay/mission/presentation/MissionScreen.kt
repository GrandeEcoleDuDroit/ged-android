package com.upsaclay.mission.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.EmptyText
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.PullToRefreshComponent
import com.upsaclay.common.presentation.components.SimpleFloatingActionButton
import com.upsaclay.common.presentation.components.TitleTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.missionsFixture
import com.upsaclay.mission.presentation.components.MissionCard
import com.upsaclay.mission.presentation.components.bottomsheet.MissionBottomSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MissionDestination(
    onMissionClick: (Int) -> Unit,
    onCreateMissionClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: MissionViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackBar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))
                is SingleUiEvent.Success -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    MissionScreen(
        missions = uiState.value.missions,
        admin = uiState.value.user?.admin == true,
        loading = uiState.value.loading,
        refreshing = uiState.value.refreshing,
        snackbarHostState = snackbarHostState,
        onMissionClick = onMissionClick,
        onCreateMissionClick = onCreateMissionClick,
        onResendMissionClick = viewModel::resendMission,
        onDeleteMissionClick = viewModel::deleteMission,
        onRefresh = viewModel::refresh,
        bottomBar = bottomBar
    )
}

@Composable
private fun MissionScreen(
    missions: List<Mission>,
    admin: Boolean,
    loading: Boolean,
    refreshing: Boolean,
    snackbarHostState: SnackbarHostState,
    onMissionClick: (Int) -> Unit,
    onCreateMissionClick: () -> Unit,
    onResendMissionClick: (Mission) -> Unit,
    onDeleteMissionClick: (Mission) -> Unit,
    onRefresh: () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteMissionDialog by remember { mutableStateOf(false) }
    var clickedMission by remember { mutableStateOf<Mission?>(null) }

    if (showDeleteMissionDialog) {
        DefaultDialog(
            text = stringResource(id = R.string.delete_mission_dialog_text),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            critical = true,
            onConfirm = {
                showDeleteMissionDialog = false
                clickedMission?.let(onDeleteMissionClick)
            },
            onCancel = { showDeleteMissionDialog = false }
        )
    }

    if (loading) {
        LoadingDialog()
    }

    MissionScaffold(
        admin = admin,
        snackbarHostState = snackbarHostState,
        onCreateMissionClick = onCreateMissionClick,
        bottomBar = bottomBar
    ) { innerPadding ->
        PullToRefreshComponent(
            modifier = Modifier.rootMediumPadding(innerPadding),
            onRefresh = onRefresh,
            refreshing = refreshing
        ) {
            LazyColumn(verticalArrangement = Arrangement.mediumSpacing()) {
                if (missions.isEmpty()) {
                    item {
                        EmptyText(text = stringResource(R.string.no_mission))
                    }
                } else {
                    items(missions) { mission ->
                        MissionCard(
                            mission = mission,
                            onClick = {
                                if (mission.state is MissionState.Published) {
                                    onMissionClick(mission.id)
                                } else {
                                    clickedMission = mission
                                    showBottomSheet = true
                                }
                            }
                        )
                    }
                }
            }
        }

        if (showBottomSheet) {
            MissionBottomSheet(
                onDismiss = { showBottomSheet = false },
                onDeleteClick = {
                    showBottomSheet = false
                    showDeleteMissionDialog = true
                },
                onResendClick = {
                    showBottomSheet = false
                    clickedMission?.let(onResendMissionClick)
                }
            )
        }
    }
}

@Composable
private fun MissionScaffold(
    admin: Boolean,
    snackbarHostState: SnackbarHostState,
    onCreateMissionClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TitleTopBar(title = stringResource(com.upsaclay.common.R.string.mission))
        },
        bottomBar = bottomBar,
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        },
        floatingActionButton = {
            if (admin) {
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
        },
        content = content
    )
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
            missions = missionsFixture,
            admin = true,
            loading = false,
            snackbarHostState = SnackbarHostState(),
            refreshing = false,
            onMissionClick = {},
            onCreateMissionClick = {},
            onResendMissionClick = {},
            onDeleteMissionClick = {},
            onRefresh = {},
            bottomBar = {}
        )
    }
}