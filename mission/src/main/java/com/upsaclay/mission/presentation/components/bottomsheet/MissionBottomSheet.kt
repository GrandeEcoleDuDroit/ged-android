package com.upsaclay.mission.presentation.components.bottomsheet

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.components.TextItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.missionFixture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionBottomSheet(
    mission: Mission,
    currentUser: User,
    onEditClick: () -> Unit,
    onResendClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val isEditable = currentUser.admin || mission.managers.contains(currentUser)

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        when (mission.state) {
            is MissionState.Error -> {
                ErrorMissionBottomSheet(
                    onDeleteClick = onDeleteClick,
                    onResendClick = onResendClick
                )
            }

            else -> {
                if (isEditable) {
                    EditableMissionBottomSheet(
                        onEditClick = onEditClick,
                        onDeleteClick = onDeleteClick
                    )
                } else {
                    NonEditableMissionBottomSheetContent(onReportClick = onReportClick)
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.large_padding)))
    }
}

@Composable
private fun EditableMissionBottomSheet(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TextItem(
        modifier = Modifier.fillMaxWidth(),
        text = { Text(text = stringResource(id = com.upsaclay.common.R.string.edit)) },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null
            )
        },
        onClick = onEditClick
    )

    TextItem(
        modifier = Modifier.fillMaxWidth(),
        text = {
            Text(
                text = stringResource(id = com.upsaclay.common.R.string.delete),
                color = MaterialTheme.colorScheme.error
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        onClick = onDeleteClick
    )
}

@Composable
private fun NonEditableMissionBottomSheetContent(
    onReportClick: () -> Unit
) {
    TextItem(
        modifier = Modifier.fillMaxWidth(),
        text = {
            Text(
                text = stringResource(id = com.upsaclay.common.R.string.report),
                color = MaterialTheme.colorScheme.error
            )
        },
        icon = {
            Icon(
                painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_report),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        onClick = onReportClick
    )
}

@Composable
private fun ErrorMissionBottomSheet(
    onResendClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TextItem(
        modifier = Modifier.fillMaxWidth(),
        text = { Text(text = stringResource(id = com.upsaclay.common.R.string.resend)) },
        icon = {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null
            )
        },
        onClick = onResendClick
    )

    TextItem(
        modifier = Modifier.fillMaxWidth(),
        text = {
            Text(
                text = stringResource(id = com.upsaclay.common.R.string.delete),
                color = MaterialTheme.colorScheme.error
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                tint = MaterialTheme.colorScheme.error,
                contentDescription = null
            )
        },
        onClick = onDeleteClick
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(heightDp = 400)
@Composable
fun EditableMissionBottomSheetPreview() {
    GedoiseTheme {
        Surface {
            MissionBottomSheet(
                mission = missionFixture,
                currentUser = userFixture,
                onEditClick = {},
                onResendClick = {},
                onReportClick = {},
                onDeleteClick = {},
                onDismiss = {}
            )
        }
    }
}

@Preview(heightDp = 400)
@Composable
fun NonEditableMissionBottomSheetPreview() {
    GedoiseTheme {
        Surface {
            MissionBottomSheet(
                mission = missionFixture,
                currentUser = userFixture.copy(admin = false),
                onEditClick = {},
                onResendClick = {},
                onReportClick = {},
                onDeleteClick = {},
                onDismiss = {}
            )
        }
    }
}

@Preview(heightDp = 400)
@Composable
fun ErrorMissionBottomSheetPreview() {
    GedoiseTheme {
        Surface {
            ErrorMissionBottomSheet(
                onResendClick = {},
                onDeleteClick = {}
            )
        }
    }
}