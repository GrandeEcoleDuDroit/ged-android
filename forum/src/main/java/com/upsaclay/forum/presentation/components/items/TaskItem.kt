package com.upsaclay.forum.presentation.components.items

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.upsaclay.common.presentation.theme.GedoiseTheme
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
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = task.value,
            modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onEditTaskClick
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = TextFieldDefaults.colors().unfocusedTrailingIconColor
                )
            }

            IconButton(
                onClick = onRemoveTaskClick
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = TextFieldDefaults.colors().unfocusedTrailingIconColor
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