package com.upsaclay.mission.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.extension.smallMediumSpacing
import com.upsaclay.common.presentation.LoadingScreen
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
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.entity.MissionReport
import com.upsaclay.mission.presentation.components.MissionCard
import com.upsaclay.mission.presentation.components.bottomsheets.MissionBottomSheet
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
    val uiState by viewModel.uiState.collectAsState()
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

    if (uiState.user != null) {
        MissionScreen(
            user = uiState.user!!,
            missions = uiState.missions,
            loading = uiState.loading,
            refreshing = uiState.refreshing,
            activeFilter = uiState.activeFilter,
            filters = uiState.filters,
            snackbarHostState = snackbarHostState,
            onMissionClick = onMissionClick,
            onCreateMissionClick = onCreateMissionClick,
            onEditMissionClick = onEditMissionClick,
            onRecreateMissionClick = viewModel::recreateMission,
            onDeleteMissionClick = viewModel::deleteMission,
            onReportMissionClick = viewModel::reportMission,
            onRefresh = viewModel::refreshMissions,
            onMissionFilterChange = viewModel::onMissionFilterChange,
            bottomBar = bottomBar
        )
    } else {
        LoadingScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MissionScreen(
    user: User,
    missions: List<Mission>?,
    loading: Boolean,
    refreshing: Boolean,
    activeFilter: MissionViewModel.MissionFilter,
    filters: List<MissionViewModel.MissionFilter>,
    snackbarHostState: SnackbarHostState,
    onMissionClick: (String) -> Unit,
    onCreateMissionClick: () -> Unit,
    onEditMissionClick: (Mission) -> Unit,
    onRecreateMissionClick: (Mission) -> Unit,
    onDeleteMissionClick: (Mission) -> Unit,
    onReportMissionClick: (MissionReport) -> Unit,
    onRefresh: () -> Unit,
    onMissionFilterChange: (MissionViewModel.MissionFilter) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    var activeBottomSheet by remember { mutableStateOf<MissionScreenBottomSheet?>(null) }
    var activeDialog by remember { mutableStateOf<MissionScreenDialog?>(null) }

    when(val dialog = activeDialog) {
        is MissionScreenDialog.DeleteMissionDialog -> {
            DefaultDialog(
                text = stringResource(id = R.string.delete_mission_dialog_text),
                confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
                critical = true,
                onConfirm = {
                    activeDialog = null
                    onDeleteMissionClick(dialog.mission)
                },
                onCancel = { activeDialog = null }
            )
        }

        else -> Unit
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
                modifier = Modifier.rootMediumPadding(innerPadding),
                onRefresh = onRefresh,
                refreshing = refreshing
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
                        LazyRow(
                            modifier = Modifier.padding(bottom = dimensionResource(com.upsaclay.common.R.dimen.small_padding)),
                            horizontalArrangement = Arrangement.smallMediumSpacing()
                        ) {
                            items(filters) { filter ->
                                FilterChip(
                                    onClick = { onMissionFilterChange(filter) },
                                    label = { Text(stringResource(filter.label)) },
                                    selected = filter == activeFilter
                                )
                            }
                        }
                    }

                    if (missions.isEmpty()) {
                        item { EmptyText(text = stringResource(R.string.no_mission)) }
                    } else {
                        items(missions) { mission ->
                            MissionCard(
                                mission = mission,
                                onClick = {
                                    if (mission.state is MissionState.Published) {
                                        onMissionClick(mission.id)
                                    } else {
                                        activeBottomSheet = MissionScreenBottomSheet.MissionBottomSheet(mission)
                                    }
                                },
                                onOptionClick = {
                                    activeBottomSheet = MissionScreenBottomSheet.MissionBottomSheet(mission)
                                }
                            )
                            Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)))
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

        when(val bottomSheet = activeBottomSheet) {
            is MissionScreenBottomSheet.MissionBottomSheet -> {
                MissionBottomSheet(
                    mission = bottomSheet.mission,
                    user = user,
                    onRecreateClick = {
                        activeBottomSheet = null
                        onRecreateMissionClick(bottomSheet.mission)
                    },
                    onEditClick = {
                        activeBottomSheet = null
                        onEditMissionClick(bottomSheet.mission)
                    },
                    onReportClick = {
                        activeBottomSheet = MissionScreenBottomSheet.MissionReportBottomSheet(bottomSheet.mission)
                    },
                    onDeleteClick = {
                        activeBottomSheet = null
                        activeDialog = MissionScreenDialog.DeleteMissionDialog(bottomSheet.mission)
                    },
                    onDismiss = { activeBottomSheet = null }
                )
            }

            is MissionScreenBottomSheet.MissionReportBottomSheet -> {
                ReportBottomSheet(
                    items = MissionReport.Reason.entries,
                    onReportClick = { reason ->
                        activeBottomSheet = null
                        onReportMissionClick(
                            MissionReport(
                                missionId = bottomSheet.mission.id,
                                reporter = MissionReport.Reporter(
                                    fullName = user.fullName,
                                    email = user.email
                                ),
                                reason = reason
                            )
                        )
                    },
                    onDismiss = { activeBottomSheet = null }
                )
            }

            else -> Unit
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

private sealed class MissionScreenBottomSheet {
    data class MissionBottomSheet(val mission: Mission): MissionScreenBottomSheet()
    data class MissionReportBottomSheet(val mission: Mission): MissionScreenBottomSheet()
}

private sealed class MissionScreenDialog {
    data class DeleteMissionDialog(val mission: Mission): MissionScreenDialog()
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun MissionScreenPreview() {
    GedoiseTheme {
        MissionScreen(
            user = userFixture,
            missions = missionsFixture,
            loading = false,
            activeFilter = MissionViewModel.MissionFilter.OPEN,
            filters = MissionViewModel.MissionFilter.entries,
            snackbarHostState = SnackbarHostState(),
            refreshing = false,
            onMissionClick = {},
            onCreateMissionClick = {},
            onEditMissionClick = {},
            onRecreateMissionClick = {},
            onDeleteMissionClick = {},
            onReportMissionClick = {},
            onRefresh = {},
            onMissionFilterChange = {},
            bottomBar = {}
        )
    }
}