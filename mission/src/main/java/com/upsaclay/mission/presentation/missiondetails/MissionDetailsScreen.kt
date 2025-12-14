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
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.mediumSpacing
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

    if (uiState.mission != null && uiState.currentUser != null) {
        MissionDetailsScreen(
            user = uiState.currentUser!!,
            mission = uiState.mission!!,
            loading = uiState.loading,
            isManager = uiState.isManager,
            buttonState = uiState.buttonState,
            snackbarHostState = snackbarHostState,
            onBackClick = onBackClick,
            onRegisterMissionClick = viewModel::registerToMission,
            onUnregisterMissionClick = viewModel::unregisterFromMission,
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
    user: User,
    mission: Mission,
    loading: Boolean,
    isManager: Boolean,
    buttonState: MissionButtonState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onRegisterMissionClick: () -> Unit,
    onUnregisterMissionClick: () -> Unit,
    onManagerClick: (User) -> Unit,
    onParticipantClick: (User) -> Unit,
    onRemoveParticipantClick: (String) -> Unit,
    onEditMissionClick: (Mission) -> Unit,
    onReportMissionClick: (MissionReport) -> Unit,
    onDeleteMissionClick: () -> Unit
) {
    var activeBottomSheet by remember { mutableStateOf<MissionDetailsScreenBottomSheet?>(null) }
    var activeDialog by remember { mutableStateOf<MissionDetailsScreenDialog?>(null) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val buttonModifier = Modifier
        .windowInsetsPadding(BottomAppBarDefaults.windowInsets)
        .padding(
            vertical = dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding),
            horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)
        )
        .fillMaxWidth()
    val hapticFeedback = LocalHapticFeedback.current

    when(val dialog = activeDialog) {
        is MissionDetailsScreenDialog.DeleteMissionDialog -> {
            DefaultDialog(
                text = stringResource(R.string.delete_mission_dialog_text),
                confirmText = stringResource(com.upsaclay.common.R.string.delete),
                critical = true,
                onConfirm = {
                    activeDialog = null
                    onDeleteMissionClick()
                },
                onCancel = { activeDialog = null }
            )
        }

        is MissionDetailsScreenDialog.UnregisterDialog -> {
            DefaultDialog(
                text = stringResource(R.string.unregister_mission_dialog_text),
                confirmText = stringResource(com.upsaclay.common.R.string.confirm),
                onConfirm = {
                    activeDialog = null
                    onUnregisterMissionClick()
                },
                onCancel = { activeDialog = null }
            )
        }

        is MissionDetailsScreenDialog.RemoveParticipantDialog -> {
            DefaultDialog(
                text = stringResource(R.string.remove_participant_dialog_text, dialog.participant.fullName),
                confirmText = stringResource(com.upsaclay.common.R.string.remove),
                critical = true,
                onConfirm = {
                    activeDialog = null
                    onRemoveParticipantClick(dialog.participant.id)
                },
                onCancel = { activeDialog = null }
            )
        }

        else -> Unit
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
                        onClick = onRegisterMissionClick
                    )
                }

                is MissionButtonState.Registered -> {
                    RegisteredButton(
                        modifier = buttonModifier,
                        loading = loading,
                        onClick = { activeDialog = MissionDetailsScreenDialog.UnregisterDialog }
                    )
                }

                is MissionButtonState.Complete -> CompleteButton(modifier = buttonModifier)

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
                verticalArrangement = Arrangement.mediumSpacing()
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

                Column(verticalArrangement = Arrangement.mediumSpacing()) {
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
                        participants = mission.participants,
                        onParticipantClick = onParticipantClick,
                        onLongParticipantClick = {
                            if (isManager) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                activeBottomSheet = MissionDetailsScreenBottomSheet.ParticipantBottomSheet(it)
                            }
                        }
                    )

                    if (mission.tasks.isNotEmpty()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                        )

                        MissionDetailsTaskSection(
                            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                            missionTasks = mission.tasks
                        )
                    }

                    if (buttonState is MissionButtonState.Hidden) {
                        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.small_padding)))
                    }
                }
            }

            MissionDetailsTopBar(
                modifier = Modifier.align(Alignment.TopStart),
                title = mission.title,
                showTitleTopBar = scrollBehavior.state.contentOffset.dp <= dimensionResource(R.dimen.image_top_bar_offset),
                onBackClick = onBackClick,
                onOptionClick = { activeBottomSheet = null }
            )
        }
    }

    when(val bottomSheet = activeBottomSheet) {
        is MissionDetailsScreenBottomSheet.MissionBottomSheet -> {
            MissionBottomSheet(
                mission = mission,
                editable = isManager,
                onEditClick = {
                    activeBottomSheet = null
                    onEditMissionClick(mission)
                },
                onReportClick = {
                    activeBottomSheet = MissionDetailsScreenBottomSheet.MissionReportBottomSheet
                },
                onDeleteClick = {
                    activeBottomSheet = null
                    activeDialog = MissionDetailsScreenDialog.DeleteMissionDialog
                },
                onDismiss = { activeBottomSheet = null }
            )
        }

        is MissionDetailsScreenBottomSheet.ParticipantBottomSheet -> {
            ModalBottomSheet(
                onDismissRequest = { activeBottomSheet = null },
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
                        activeBottomSheet = null
                        activeDialog = MissionDetailsScreenDialog.RemoveParticipantDialog(bottomSheet.participant)
                    }
                )

                Spacer(Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.modal_bottom_sheet_bottom_space)))
            }
        }

        is MissionDetailsScreenBottomSheet.MissionReportBottomSheet -> {
            ReportBottomSheet(
                items = MissionReport.Reason.entries,
                onDismiss = { activeBottomSheet = null },
                onReportClick = { reason ->
                    activeBottomSheet = null
                    onReportMissionClick(
                        MissionReport(
                            missionId = mission.id,
                            reporter = MissionReport.Reporter(
                                fullName = user.fullName,
                                email = user.email
                            ),
                            reason = reason
                        )
                    )
                }
            )
        }

        else -> Unit
    }
}

@Composable
private fun RegisterButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit
) {
    LoadingButton(
        modifier = modifier,
        text = stringResource(R.string.register_mission_button_text),
        loading = loading,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
private fun RegisteredButton(
    modifier: Modifier = Modifier,
    loading: Boolean,
    onClick: () -> Unit
) {
    LoadingButton(
        modifier = modifier,
        text = stringResource(R.string.registered_mission_button_text),
        loading = loading,
        onClick = onClick,
        colors = MaterialTheme.colorScheme.activatedButtonColors
    )
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

private sealed class MissionDetailsScreenBottomSheet {
    data object MissionBottomSheet: MissionDetailsScreenBottomSheet()
    data class ParticipantBottomSheet(val participant: User): MissionDetailsScreenBottomSheet()
    data object MissionReportBottomSheet: MissionDetailsScreenBottomSheet()
}

private sealed class MissionDetailsScreenDialog {
    data object DeleteMissionDialog: MissionDetailsScreenDialog()
    data object UnregisterDialog: MissionDetailsScreenDialog()
    data class RemoveParticipantDialog(val participant: User): MissionDetailsScreenDialog()
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
                onRegisterMissionClick = {},
                onUnregisterMissionClick = {},
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