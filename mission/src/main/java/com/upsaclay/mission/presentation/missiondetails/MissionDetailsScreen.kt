package com.upsaclay.mission.presentation.missiondetails

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.LoadingButton
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.components.TextItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.activatedButtonColors
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.entity.MissionReport
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.presentation.components.MissionImage
import com.upsaclay.mission.presentation.components.bottomsheets.MissionBottomSheet
import com.upsaclay.mission.presentation.missiondetails.MissionDetailsViewModel.MissionButtonState
import com.upsaclay.mission.presentation.missiondetails.MissionDetailsViewModel.MissionDetailsUiEvent
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MissionDetailsDestination(
    onBackClick: () -> Unit,
    missionId: String,
    onManagerClick: (User) -> Unit,
    onParticipantClick: (User) -> Unit,
    onEditMissionClick: (Mission) -> Unit,
    viewModel: MissionDetailsViewModel = koinViewModel(
        parameters = { parametersOf(missionId) }
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
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
                is MissionDetailsUiEvent.MissionDetailsDeleted -> onBackClick()
            }
        }
    }

    if (uiState.mission != null && uiState.user != null) {
        MissionDetailsScreen(
            onBackClick = onBackClick,
            user = uiState.user!!,
            mission = uiState.mission!!,
            loading = uiState.loading,
            isManager = uiState.isManager,
            buttonState = uiState.buttonState,
            snackbarHostState = snackbarHostState,
            onRegisterClick = viewModel::registerToMission,
            onUnregisterClick = viewModel::unregisterFromMission,
            onManagerClick = onManagerClick,
            onParticipantClick = onParticipantClick,
            onRemoveParticipantClick = viewModel::removeParticipant,
            onEditMissionClick = onEditMissionClick,
            onReportMissionClick = viewModel::reportMission,
            onDeleteMissionClick = viewModel::deleteMission
        )
    } else {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressBar(
                modifier = Modifier.padding(top = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MissionDetailsScreen(
    onBackClick: () -> Unit,
    user: User,
    mission: Mission,
    loading: Boolean,
    isManager: Boolean,
    buttonState: MissionButtonState,
    snackbarHostState: SnackbarHostState,
    onRegisterClick: () -> Unit,
    onUnregisterClick: () -> Unit,
    onManagerClick: (User) -> Unit,
    onParticipantClick: (User) -> Unit,
    onRemoveParticipantClick: (String) -> Unit,
    onEditMissionClick: (Mission) -> Unit,
    onReportMissionClick: (MissionReport) -> Unit,
    onDeleteMissionClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val buttonModifier = Modifier
        .windowInsetsPadding(BottomAppBarDefaults.windowInsets)
        .padding(
            vertical = dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding),
            horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)
        )
        .fillMaxWidth()

    var showMissionBottomSheet by remember { mutableStateOf(false) }
    var showParticipantBottomSheet by remember { mutableStateOf(false) }
    var showReportBottomSheet by remember { mutableStateOf(false) }

    var showDeleteMissionDialog by remember { mutableStateOf(false) }
    var showUnregisterDialog by remember { mutableStateOf(false) }
    var showRemoveParticipantDialog by remember { mutableStateOf(false) }

    var clickedParticipant by remember { mutableStateOf<User?>(null) }
    val hapticFeedback = LocalHapticFeedback.current

    if (showDeleteMissionDialog) {
        DefaultDialog(
            text = stringResource(R.string.delete_mission_dialog_text),
            confirmText = stringResource(com.upsaclay.common.R.string.delete),
            critical = true,
            onConfirm = {
                showDeleteMissionDialog = false
                onDeleteMissionClick()
            },
            onCancel = { showDeleteMissionDialog = false }
        )
    }

    if (showUnregisterDialog) {
        DefaultDialog(
            text = stringResource(R.string.unregister_mission_dialog_text),
            confirmText = stringResource(com.upsaclay.common.R.string.confirm),
            onConfirm = {
                showUnregisterDialog = false
                onUnregisterClick()
            },
            onCancel = { showUnregisterDialog = false }
        )
    }

    if (showRemoveParticipantDialog) {
        clickedParticipant?.let {
            DefaultDialog(
                text = stringResource(R.string.remove_participant_dialog_text, it.fullName),
                confirmText = stringResource(com.upsaclay.common.R.string.remove),
                critical = true,
                onConfirm = {
                    showRemoveParticipantDialog = false
                    onRemoveParticipantClick(it.id)
                },
                onCancel = { showRemoveParticipantDialog = false }
            )
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            when (buttonState) {
                is MissionButtonState.Register -> {
                    RegisterButton(
                        modifier = buttonModifier,
                        enabled = buttonState.enabled,
                        loading = loading,
                        onClick = onRegisterClick
                    )
                }

                is MissionButtonState.Registered -> {
                    RegisteredButton(
                        modifier = buttonModifier,
                        loading = loading,
                        onClick = { showUnregisterDialog = true }
                    )
                }

                is MissionButtonState.Complete -> {
                    CompleteButton(modifier = buttonModifier)
                }

                else -> Unit
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(snackbarData = it)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.smallSpacing()
            ) {
                MissionImage(
                    modifier = Modifier.height(dimensionResource(R.dimen.mission_image_height)),
                    model = when (val state = mission.state) {
                        is MissionState.Draft -> null
                        is MissionState.Publishing -> state.imagePath
                        is MissionState.Published -> state.imageUrl
                        is MissionState.Error -> state.imagePath
                    },
                    defaultImageScale = 1.6f
                )

                Column(
                    modifier = Modifier.padding(top  = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                    verticalArrangement = Arrangement.mediumSpacing()
                ) {
                    MissionDetailsTitleAndDescriptionSection(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                        mission = mission
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                    )

                    MissionDetailsInformationSection(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                        mission = mission
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                    )

                    MissionDetailsManagerSection(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                        managers = mission.managers,
                        onManagerClick = onManagerClick
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                    )

                    MissionDetailsParticipantSection(
                        modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                        users = mission.participants,
                        onParticipantClick = onParticipantClick,
                        onLongParticipantClick = {
                            if (isManager) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                clickedParticipant = it
                                showParticipantBottomSheet = true
                            }
                        }
                    )

                    if (mission.tasks.isNotEmpty()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                        )

                        MissionDetailsTaskSection(
                            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                            tasks = mission.tasks
                        )
                    }

                    if (buttonState is MissionButtonState.Hidden) {
                        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.small_padding)))
                    }
                }
            }

            MissionTopBar(
                modifier = Modifier.align(Alignment.TopStart),
                scrollBehavior = scrollBehavior,
                title = mission.title,
                onBackClick = onBackClick,
                onOptionClick = { showMissionBottomSheet = true }
            )
        }
    }

    if (showMissionBottomSheet) {
        MissionBottomSheet(
            mission = mission,
            editable = mission.managers.any { it.id == user.id },
            onEditClick = {
                showMissionBottomSheet = false
                onEditMissionClick(mission)
            },
            onReportClick = {
                showMissionBottomSheet = false
                showReportBottomSheet = true
            },
            onDeleteClick = {
                showMissionBottomSheet = false
                showDeleteMissionDialog = true
            },
            onDismiss = { showMissionBottomSheet = false }
        )
    }

    if (showParticipantBottomSheet) {
        clickedParticipant?.let {
            ModalBottomSheet(
                onDismissRequest = { showParticipantBottomSheet = false },
            ) {
                TextItem(
                    text = {
                        Text(
                            text = stringResource(com.upsaclay.common.R.string.remove),
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_outline_remove_person),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    onClick = {
                        showParticipantBottomSheet = false
                        showRemoveParticipantDialog = true
                    }
                )

                Spacer(Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.modal_bottom_sheet_bottom_space)))
            }
        }
    }

    if (showReportBottomSheet) {
        ReportBottomSheet(
            items = MissionReport.Reason.entries,
            onDismiss = { showReportBottomSheet = false },
            onReportClick = { reason ->
                showReportBottomSheet = false
                onReportMissionClick(
                    MissionReport(
                        missionId = mission.id,
                        userInfo = MissionReport.UserInfo(
                            fullName = user.fullName,
                            email = user.email
                        ),
                        reason = reason
                    )
                )
            }
        )
    }
}

@Composable
private fun RegisterButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit
) {
    if (loading) {
        LoadingButton(modifier = modifier)
    } else {
        PrimaryButton(
            modifier = modifier,
            text = stringResource(R.string.register_mission_button_text),
            enabled = enabled,
            onClick = onClick
        )
    }
}

@Composable
private fun RegisteredButton(
    modifier: Modifier = Modifier,
    loading: Boolean,
    onClick: () -> Unit
) {
    if (loading) {
        LoadingButton(modifier = modifier)
    } else {
        Button(
            modifier = modifier,
            colors = MaterialTheme.colorScheme.activatedButtonColors,
            onClick = onClick
        ) {
            Text(text = stringResource(R.string.registered_mission_button_text))
        }
    }
}

@Composable
private fun CompleteButton(modifier: Modifier = Modifier) {
    PrimaryButton(
        modifier = modifier,
        text = stringResource(R.string.complete_mission_button_text),
        enabled = false,
        onClick = {}
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun MissionDetailsScreenPreview() {
    GedoiseTheme {
        Surface {
            MissionDetailsScreen(
                user = userFixture,
                mission = missionFixture,
                loading = false,
                isManager = true,
                buttonState = MissionButtonState.Register(),
                snackbarHostState = SnackbarHostState(),
                onRegisterClick = {},
                onUnregisterClick = {},
                onBackClick = {},
                onManagerClick = {},
                onParticipantClick = {},
                onRemoveParticipantClick = {},
                onEditMissionClick = {},
                onReportMissionClick = {},
                onDeleteMissionClick = {}
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RegisterButtonPreview() {
    GedoiseTheme {
        RegisterButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            loading = false,
            onClick = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RegisteredButtonPreview() {
    GedoiseTheme {
        RegisteredButton(
            modifier = Modifier.fillMaxWidth(),
            loading = false,
            onClick = {}
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CompleteButtonPreview() {
    GedoiseTheme {
        CompleteButton(modifier = Modifier.fillMaxWidth())
    }
}