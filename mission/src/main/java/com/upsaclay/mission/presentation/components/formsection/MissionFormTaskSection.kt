package com.upsaclay.mission.presentation.components.formsection

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.leadingIcon
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.tasksFixture
import com.upsaclay.mission.presentation.components.item.MissionTaskItem
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay

@Composable
fun MissionFormTaskSection(
    missionTasks: List<MissionTask>,
    scrollState: ScrollState,
    onTaskClick: (MissionTask) -> Unit,
    onAddTaskClick: () -> Unit,
    onRemoveTaskClick: (MissionTask) -> Unit
) {
    var currentSize by remember { mutableIntStateOf(missionTasks.size) }

    LaunchedEffect(missionTasks) {
        if (missionTasks.size > currentSize) {
            awaitFrame()
            delay(200)
            scrollState.animateScrollTo(scrollState.maxValue)
            currentSize = missionTasks.size
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            text = stringResource(R.string.tasks),
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.small_padding)))

        Row(
            modifier = Modifier
                .clickable(onClick = onAddTaskClick)
                .padding(
                    horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
                    vertical = dimensionResource(com.upsaclay.common.R.dimen.small_medium_padding)
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.smallSpacing(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_outline_add_task),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.leadingIcon
            )

            Text(text = stringResource(R.string.add_task))
        }

        Column {
            missionTasks.forEach {
                MissionTaskItem(
                    missionTask = it,
                    onTaskClick = { onTaskClick(it) },
                    onRemoveTaskClick = { onRemoveTaskClick(it) }
                )
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
private fun CreateMissionTaskSectionPreview() {
    GedoiseTheme {
        Surface {
            MissionFormTaskSection(
                missionTasks = tasksFixture,
                scrollState = ScrollState(0),
                onAddTaskClick = {},
                onTaskClick = {},
                onRemoveTaskClick = {}
            )
        }
    }
}
