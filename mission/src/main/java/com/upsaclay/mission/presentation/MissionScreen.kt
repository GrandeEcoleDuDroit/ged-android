package com.upsaclay.mission.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.EmptyText
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.PullToRefreshComponent
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.components.SimpleFloatingActionButton
import com.upsaclay.common.presentation.components.TitleTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionReport
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.missionsFixture
import com.upsaclay.mission.presentation.components.MissionCard
import com.upsaclay.mission.presentation.components.bottomsheet.MissionBottomSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MissionDestination(
    onMissionClick: (String) -> Unit,
    onCreateMissionClick: () -> Unit,
    onEditMissionClick: (Mission) -> Unit,
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

    if (uiState.value.user != null) {
        MissionScreen(
            user = uiState.value.user!!,
            missions = uiState.value.missions,
            loading = uiState.value.loading,
            refreshing = uiState.value.refreshing,
            snackbarHostState = snackbarHostState,
            onMissionClick = onMissionClick,
            onCreateMissionClick = onCreateMissionClick,
            onEditMissionClick = onEditMissionClick,
            onResendMissionClick = viewModel::resendMission,
            onDeleteMissionClick = viewModel::deleteMission,
            onReportMissionClick = viewModel::reportMission,
            onRefresh = viewModel::refreshMissions,
            bottomBar = bottomBar
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MissionScreen(
    user: User,
    missions: List<Mission>?,
    loading: Boolean,
    refreshing: Boolean,
    snackbarHostState: SnackbarHostState,
    onMissionClick: (String) -> Unit,
    onCreateMissionClick: () -> Unit,
    onEditMissionClick: (Mission) -> Unit,
    onResendMissionClick: (Mission) -> Unit,
    onDeleteMissionClick: (Mission) -> Unit,
    onReportMissionClick: (MissionReport) -> Unit,
    onRefresh: () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    var showMissionBottomSheet by remember { mutableStateOf(false) }
    var showDeleteMissionDialog by remember { mutableStateOf(false) }
    var clickedMission by remember { mutableStateOf<Mission?>(null) }
    var showMissionReportBottomSheet by remember { mutableStateOf(false) }

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
        admin = user.admin,
        snackbarHostState = snackbarHostState,
        onCreateMissionClick = onCreateMissionClick,
        bottomBar = bottomBar
    ) { innerPadding ->
        missions?.let { missions ->
            PullToRefreshComponent(
                modifier = Modifier.padding(innerPadding),
                onRefresh = onRefresh,
                refreshing = refreshing
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                    verticalArrangement = Arrangement.mediumSpacing()
                ) {
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
                                        showMissionBottomSheet = true
                                    }
                                },
                                onOptionClick = {
                                    clickedMission = mission
                                    showMissionBottomSheet = true
                                }
                            )
                        }
                    }
                }
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .padding(top = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                contentAlignment = Alignment.TopCenter
            ) {
                CircularProgressBar()
            }
        }

        if (showMissionBottomSheet) {
            clickedMission?.let { mission ->
                MissionBottomSheet(
                    mission = mission,
                    currentUser = user,
                    onResendClick = {
                        showMissionBottomSheet = false
                        onResendMissionClick(mission)
                    },
                    onEditClick = {
                        showMissionBottomSheet = false
                        onEditMissionClick(mission)
                    },
                    onReportClick = {
                        showMissionBottomSheet = false
                        showMissionReportBottomSheet = true
                    },
                    onDeleteClick = {
                        showMissionBottomSheet = false
                        showDeleteMissionDialog = true
                    },
                    onDismiss = { showMissionBottomSheet = false }
                )
            }
        }

        if (showMissionReportBottomSheet) {
            ReportBottomSheet(
                items = MissionReport.Reason.entries,
                onDismiss = { showMissionReportBottomSheet = false },
                onReportClick = { reason ->
                    showMissionReportBottomSheet = false

                    clickedMission?.let { mission ->
                        onReportMissionClick(
                            MissionReport(
                                missionId = mission.id,
                                userInfo = MissionReport.UserInfo(
                                    fullName = user.fullName,
                                    email = user.email
                                ),
                                reason = reason,
                            )
                        )
                    }
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
            user = userFixture,
            missions = missionsFixture,
            loading = false,
            snackbarHostState = SnackbarHostState(),
            refreshing = false,
            onMissionClick = {},
            onCreateMissionClick = {},
            onEditMissionClick = {},
            onResendMissionClick = {},
            onDeleteMissionClick = {},
            onReportMissionClick = {},
            onRefresh = {},
            bottomBar = {}
        )
    }
}