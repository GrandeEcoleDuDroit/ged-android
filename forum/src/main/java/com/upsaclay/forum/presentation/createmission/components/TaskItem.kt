package com.upsaclay.forum.presentation.createmission.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.domain.entity.Task
import com.upsaclay.forum.domain.taskFixture

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onEditTaskClick: () -> Unit,
    onRemoveTaskClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(vertical = MaterialTheme.spacing.small)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = task.value
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                modifier = Modifier.size(40.dp),
                onClick = onEditTaskClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }

            IconButton(
                modifier = Modifier.size(40.dp),
                onClick = onRemoveTaskClick
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null
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
private fun TaskItemPreview() {
    var task by remember { mutableStateOf(taskFixture) }

    GedoiseTheme {
        Surface {
            TaskItem(
                task = task,
                onEditTaskClick = {},
                onRemoveTaskClick = {}
            )
        }
    }
}