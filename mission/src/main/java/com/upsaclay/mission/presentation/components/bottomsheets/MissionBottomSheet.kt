package com.upsaclay.mission.presentation.components.bottomsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.components.TextItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.missionFixture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionBottomSheet(
    mission: Mission,
    user: User,
    onEditClick: () -> Unit,
    onRecreateClick: () -> Unit = {},
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.navigationBarsPadding()) {
            when (mission.state) {
                is MissionState.Published -> {
                    if (user.admin || mission.managers.contains(user)) {
                        EditableMissionBottomSheetContent(
                            admin = user.admin,
                            onEditClick = onEditClick,
                            onDeleteClick = onDeleteClick
                        )
                    } else {
                        NonEditableMissionBottomSheetContent(onReportClick = onReportClick)
                    }
                }

                is MissionState.Publishing -> PublishingMissionBottomSheetContent(onDeleteClick = onDeleteClick)

                is MissionState.Error -> {
                    ErrorMissionBottomSheetContent(
                        onDeleteClick = onDeleteClick,
                        onRecreateClick = onRecreateClick
                    )
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun EditableMissionBottomSheetContent(
    admin: Boolean,
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

    if (admin) {
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
}

@Composable
private fun PublishingMissionBottomSheetContent(
    onDeleteClick: () -> Unit
) {
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
private fun ErrorMissionBottomSheetContent(
    onRecreateClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TextItem(
        modifier = Modifier.fillMaxWidth(),
        text = { Text(text = stringResource(id = com.upsaclay.common.R.string.retry)) },
        icon = {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null
            )
        },
        onClick = onRecreateClick
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
                user = userFixture,
                onEditClick = {},
                onRecreateClick = {},
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
                user = userFixture,
                onEditClick = {},
                onRecreateClick = {},
                onReportClick = {},
                onDeleteClick = {},
                onDismiss = {}
            )
        }
    }
}

@Preview(heightDp = 400)
@Composable
fun ErrorMissionBottomSheetContentPreview() {
    GedoiseTheme {
        Surface {
            ErrorMissionBottomSheetContent(
                onRecreateClick = {},
                onDeleteClick = {}
            )
        }
    }
}