package com.upsaclay.forum.presentation.createmission

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.extension.largeSpacing
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.LargeAsyncImage
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.components.SimpleOutlinedTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.backgroundLoadingImage
import com.upsaclay.common.presentation.theme.darkGray
import com.upsaclay.common.presentation.theme.inputBackground
import com.upsaclay.common.presentation.theme.lightGray
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.R
import com.upsaclay.forum.domain.entity.Task
import com.upsaclay.forum.domain.taskFixture
import com.upsaclay.forum.domain.tasksFixture
import com.upsaclay.forum.presentation.createmission.components.DatePicker
import com.upsaclay.forum.presentation.components.ManagerItem
import com.upsaclay.forum.presentation.createmission.components.SchoolLevelDropDownMenu
import com.upsaclay.forum.presentation.createmission.components.TaskItem
import java.time.LocalDateTime

@Composable
fun CreateMissionForm(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    schoolLevels: List<SchoolLevel>,
    selectedSchoolLevels: List<SchoolLevel>,
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    frequency: String,
    selectedManagers: List<User>,
    tasks: List<Task>,
    imageUri: Uri?,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSelectedSchoolLevelChange: (SchoolLevel) -> Unit,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onFrequencyChange: (String) -> Unit,
    onShowManagerListClick: () -> Unit,
    onAddTaskClick: () -> Unit,
    onEditTaskClick: (Task) -> Unit,
    onRemoveTaskClick: (Task) -> Unit,
    onImageClick: () -> Unit,
    onRemoveImageClick: () -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        ImageSection(
            imageUri = imageUri,
            onImageClick = onImageClick,
            onRemoveImageClick = onRemoveImageClick
        )

        Column(verticalArrangement = Arrangement.largeSpacing()) {
            TitleDescriptionSection(
                title = title,
                description = description,
                onTitleChange = onTitleChange,
                onDescriptionChange = onDescriptionChange
            )

            InformationSection(
                schoolLevels = schoolLevels,
                selectedSchoolLevels = selectedSchoolLevels,
                startDate = startDate,
                endDate = endDate,
                frequency = frequency,
                onSelectedSchoolLevelsChange = onSelectedSchoolLevelChange,
                onStartDateClick = onStartDateClick,
                onEndDateClick = onEndDateClick,
                onFrequencyChange = onFrequencyChange,
            )

            ManagerSection(
                managers = selectedManagers,
                onShowManagerListClick = onShowManagerListClick
            )

            TaskSection(
                tasks = tasks,
                onAddTaskClick = onAddTaskClick,
                onEditTaskClick = onEditTaskClick,
                onRemoveTaskClick = onRemoveTaskClick
            )
        }
    }
}

@Composable
private fun ImageSection(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    onImageClick: () -> Unit,
    onRemoveImageClick: () -> Unit
) {
    var isError by remember { mutableStateOf(false) }
    val defaultImage = painterResource(R.drawable.ic_outline_add_image)
    val iconColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.lightGray
    } else {
        MaterialTheme.colorScheme.darkGray
    }

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.backgroundLoadingImage)
            .fillMaxWidth()
            .height(200.dp)
            .clickable(onClick = onImageClick),
        contentAlignment = Alignment.Center
    ) {
        imageUri?.let {
            Box {
                LargeAsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = it,
                    onError = {
                        isError = true
                        defaultImage
                    }
                )

                FilledTonalButton(
                    onClick = onRemoveImageClick,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.inputBackground
                    )
                ) {
                    Text(
                        text = stringResource(com.upsaclay.common.R.string.remove),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        } ?: run {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(fraction = 0.4f),
                    painter = defaultImage,
                    contentDescription = null,
                    tint = iconColor
                )

                Text(
                    text = stringResource(R.string.add_image),
                    color = iconColor
                )
            }
        }
    }
}

@Composable
private fun TitleDescriptionSection(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    SelectionContainer {
        Column(
            modifier = modifier.padding(horizontal = MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            SimpleOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                onValueChange = onTitleChange,
                label = stringResource(R.string.title),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrectEnabled = true
                )
            )

            SimpleOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = onDescriptionChange,
                label = stringResource(R.string.description),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrectEnabled = true
                ),
                singleLine = false,
                minLines = 4
            )
        }
    }
}

@Composable
private fun InformationSection(
    modifier: Modifier = Modifier,
    schoolLevels: List<SchoolLevel>,
    selectedSchoolLevels: List<SchoolLevel>,
    startDate: LocalDateTime,
    endDate: LocalDateTime,
    frequency: String,
    onSelectedSchoolLevelsChange: (SchoolLevel) -> Unit,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onFrequencyChange: (String) -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        SectionTitle(text = stringResource(R.string.informations))

        SchoolLevelDropDownMenu(
            schoolLevels = schoolLevels,
            selectedSchoolLevels = selectedSchoolLevels,
            onSelectedSchoolLevelsChange = onSelectedSchoolLevelsChange
        )

        DatePicker(
            date = startDate,
            onClick = onStartDateClick,
            label = stringResource(R.string.start_date)
        )

        DatePicker(
            date = endDate,
            onClick = onEndDateClick,
            label = stringResource(R.string.end_date)
        )

        SelectionContainer {
            SimpleOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = frequency,
                onValueChange = onFrequencyChange,
                label = stringResource(R.string.frequency),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_schedule),
                        contentDescription = null,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrectEnabled = true
                )
            )
        }
    }
}

@Composable
private fun ManagerSection(
    modifier: Modifier = Modifier,
    managers: List<User>,
    onShowManagerListClick: () -> Unit
) {
    val imageScale = 0.5f
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        SectionTitle(text = stringResource(R.string.mission_managers))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.mediumSpacing()
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.smallSpacing(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        modifier = Modifier
                            .clip(ShapeDefaults.ExtraLarge)
                            .width(48.dp)
                            .background(MaterialTheme.colorScheme.inputBackground),
                        onClick = onShowManagerListClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_outline_add_person),
                            contentDescription = stringResource(R.string.add_manager)
                        )
                    }

                    Text(
                        text = stringResource(R.string.add_manager),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            items(managers) {
                ManagerItem(
                    user = it,
                    imageScale = imageScale
                )
            }
        }
    }
}

@Composable
private fun TaskSection(
    tasks: List<Task>,
    onAddTaskClick: () -> Unit,
    onEditTaskClick: (Task) -> Unit,
    onRemoveTaskClick: (Task) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
                text = stringResource(R.string.tasks)
            )

            IconButton(
                onClick = onAddTaskClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_task),
                )
            }
        }

        SelectionContainer {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                tasks.forEach { task ->
                    TaskItem(
                        modifier = Modifier.padding(start = MaterialTheme.spacing.medium),
                        task = task,
                        onEditTaskClick = { onEditTaskClick(task) },
                        onRemoveTaskClick = { onRemoveTaskClick(task) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleMedium,
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun CreateMissionFormPreview() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(tasksFixture) }
    var managers by remember { mutableStateOf(listOf(userFixture, userFixture2)) }

    GedoiseTheme {
        Surface {
            CreateMissionForm(
                title = title,
                description = description,
                schoolLevels = SchoolLevel.entries,
                selectedSchoolLevels = emptyList(),
                startDate = LocalDateTime.now(),
                endDate = LocalDateTime.now(),
                frequency = frequency,
                selectedManagers = managers,
                imageUri = null,
                tasks = tasks,
                onTitleChange = { title = it },
                onDescriptionChange = { description = it },
                onSelectedSchoolLevelChange = {},
                onStartDateClick = {},
                onEndDateClick = {},
                onFrequencyChange = { frequency = it },
                onShowManagerListClick = {},
                onAddTaskClick = { tasks += taskFixture },
                onEditTaskClick = {},
                onRemoveTaskClick = {},
                onImageClick = {},
                onRemoveImageClick = {}
            )
        }
    }
}
