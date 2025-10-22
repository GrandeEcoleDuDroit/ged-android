package com.upsaclay.mission.presentation.components.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.leadingIcon
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.missionTaskFixture

@Composable
fun MissionTaskItem(
    modifier: Modifier = Modifier,
    missionTask: MissionTask,
    onTaskClick: () -> Unit,
    onRemoveTaskClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onTaskClick)
            .padding(start = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = missionTask.value,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onRemoveTaskClick) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.leadingIcon
            )
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
private fun TaskItemPreview() {
    var task by remember { mutableStateOf(missionTaskFixture) }

    GedoiseTheme {
        Surface {
            MissionTaskItem(
                missionTask = task,
                onTaskClick = {},
                onRemoveTaskClick = {}
            )
        }
    }
}