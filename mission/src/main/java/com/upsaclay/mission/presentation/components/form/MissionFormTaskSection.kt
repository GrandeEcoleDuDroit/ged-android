package com.upsaclay.mission.presentation.components.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.SectionTitle
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.missionTasksFixture
import com.upsaclay.mission.presentation.components.RemoveButton

@Composable
fun MissionFormTaskSection(
    missionTasks: List<MissionTask>,
    onTaskClick: (MissionTask) -> Unit,
    onAddTaskClick: () -> Unit,
    onRemoveTaskClick: (MissionTask) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(
            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            title = stringResource(R.string.tasks)
        )

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.small_padding)))

        AddMissionTaskItem(
            modifier = Modifier
                .clickable(onClick = onAddTaskClick)
                .padding(
                    horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
                    vertical = 14.dp
                )
                .fillMaxWidth()
        )

        missionTasks.forEach {
            MissionTaskItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { onTaskClick(it) })
                    .padding(
                        horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
                        vertical = 10.dp
                    ),
                missionTask = it,
                onRemoveTaskClick = { onRemoveTaskClick(it) }
            )
        }
    }
}

@Composable
private fun AddMissionTaskItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.smallSpacing(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = stringResource(R.string.add_task),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MissionTaskItem(
    modifier: Modifier = Modifier,
    missionTask: MissionTask,
    onRemoveTaskClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = missionTask.value,
            modifier = Modifier.weight(1f)
        )

        RemoveButton(
            onClick = onRemoveTaskClick
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun MissionFormTaskSectionPreview() {
    GedoiseTheme {
        Surface {
            MissionFormTaskSection(
                missionTasks = missionTasksFixture,
                onAddTaskClick = {},
                onTaskClick = {},
                onRemoveTaskClick = {}
            )
        }
    }
}
