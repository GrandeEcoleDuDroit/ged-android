package com.upsaclay.forum.presentation.createmission

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.R
import com.upsaclay.forum.domain.entity.Task
import com.upsaclay.forum.domain.taskFixture
import com.upsaclay.forum.domain.tasksFixture
import com.upsaclay.forum.presentation.components.items.TaskItem
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay

@Composable
fun CreateMissionTaskSection(
    tasks: List<Task>,
    scrollState: ScrollState,
    onAddTaskClick: () -> Unit,
    onEditTaskClick: (Task) -> Unit,
    onRemoveTaskClick: (Task) -> Unit
) {
    var currentSize by remember { mutableIntStateOf(tasks.size) }

    LaunchedEffect(tasks) {
        if (tasks.size > currentSize) {
            awaitFrame()
            delay(200)
            scrollState.animateScrollTo(scrollState.maxValue)
            currentSize = tasks.size
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
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
                tint = TextFieldDefaults.colors().unfocusedLeadingIconColor
            )

            Text(
                text = stringResource(R.string.add_task)
            )
        }

        SelectionContainer {
            Column {
                tasks.forEach {
                    TaskItem(
                        modifier = Modifier
                            .padding(start = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                        task = it,
                        onEditTaskClick = { onEditTaskClick(it) },
                        onRemoveTaskClick = { onRemoveTaskClick(it) }
                    )
                }
            }
        }
    }
}

@Phones
@Composable
private fun CreateMissionTaskSectionPreview() {
    var tasks by remember { mutableStateOf(tasksFixture) }

    GedoiseTheme {
        Surface {
            CreateMissionTaskSection(
                tasks = tasks,
                scrollState = ScrollState(0),
                onAddTaskClick = { tasks = tasks + taskFixture },
                onEditTaskClick = {},
                onRemoveTaskClick = { tasks = tasks - it }
            )
        }
    }
}
