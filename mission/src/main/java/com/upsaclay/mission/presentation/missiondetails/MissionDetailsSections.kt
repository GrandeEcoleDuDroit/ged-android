package com.upsaclay.mission.presentation.missiondetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.common.extension.extraSmallSpacing
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallMediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.EmptyText
import com.upsaclay.common.presentation.components.UserItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.presentation.components.items.MissionInformationItem
import com.upsaclay.mission.presentation.components.items.MissionUserItem
import com.upsaclay.mission.presentation.components.items.SectionTitle

@Composable
fun MissionDetailsTitleAndDescriptionSection(
    modifier: Modifier = Modifier,
    mission: Mission
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        Text(
            text = mission.title,
            style = titleStyle
        )

        Text(
            text = mission.description,
            style = descriptionStyle
        )
    }
}

@Composable
fun MissionDetailsInformationSection(
    modifier: Modifier = Modifier,
    mission: Mission
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallMediumSpacing()
    ) {
        SectionTitle(title = stringResource(R.string.information))

        MissionInformationItem(
            modifier = Modifier.fillMaxWidth(),
            mission = mission,
            textStyle = contentStyle
        )
    }
}

@Composable
fun MissionDetailsManagerSection(
    modifier: Modifier = Modifier,
    managers: List<User>,
    onManagerClick: (User) -> Unit
) {
    Column(verticalArrangement = Arrangement.extraSmallSpacing()) {
        SectionTitle(
            modifier = modifier,
            title = stringResource(R.string.managers)
        )

        LazyColumn(
            modifier = Modifier.heightIn(max = 200.dp)
        ) {
            items(managers) {
                MissionUserItem(
                    modifier = Modifier.clickable { onManagerClick(it) },
                    user = it,
                    imageScale = 0.4f,
                    showAdminIndicator = false,
                    textStyle = contentStyle
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MissionDetailsParticipantSection(
    modifier: Modifier = Modifier,
    users: List<User>,
    onParticipantClick: (User) -> Unit,
    onLongParticipantClick: (User) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.extraSmallSpacing()
    ) {
        SectionTitle(
            modifier = modifier,
            title = stringResource(R.string.participants)
        )

        LazyColumn(
            modifier = Modifier.heightIn(max = 200.dp)
        ) {
            if (users.isEmpty()) {
                item {
                    EmptyText(
                        text = stringResource(R.string.no_participants),
                        textStyle = contentStyle
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding)))
                }
            } else {
                items(users) {
                    UserItem(
                        modifier = Modifier.combinedClickable(
                            onClick = { onParticipantClick(it) },
                            onLongClick = { onLongParticipantClick(it) }
                        ),
                        user = it,
                        imageScale = 0.4f,
                        textStyle = contentStyle
                    )
                }
            }
        }
    }
}

@Composable
fun MissionDetailsTaskSection(
    modifier: Modifier = Modifier,
    tasks: List<MissionTask>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        SectionTitle(title = stringResource(R.string.tasks))

        Column(
            verticalArrangement = Arrangement.smallMediumSpacing()
        ) {
            tasks.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.smallSpacing()
                ) {
                    Text(text = "\u2022", fontSize = 20.sp)
                    Text(text = it.value, style = contentStyle)
                }
            }
        }
    }
}

private val titleStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.titleLarge

private val descriptionStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.bodyLarge

private val contentStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.bodyMedium

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun MissionDetailsTitleAndDescriptionSectionPreview() {
    GedoiseTheme {
        Surface {
            MissionDetailsTitleAndDescriptionSection(
                mission = missionFixture
            )
        }
    }
}

@PhonePreviews
@Composable
private fun MissionDetailsInformationSectionPreview() {
    GedoiseTheme {
        Surface {
            MissionDetailsInformationSection(mission = missionFixture)
        }
    }
}

@PhonePreviews
@Composable
private fun MissionDetailsManagerSectionPreview() {
    GedoiseTheme {
        Surface {
            MissionDetailsManagerSection(
                managers = usersFixture,
                onManagerClick = {}
            )
        }
    }
}

@PhonePreviews
@Composable
private fun MissionDetailsParticipantSectionPreview() {
    GedoiseTheme {
        Surface {
            MissionDetailsParticipantSection(
                users = emptyList(),
                onParticipantClick = {},
                onLongParticipantClick = {}
            )
        }
    }
}

@PhonePreviews
@Composable
private fun MissionDetailsTaskSectionPreview() {
    GedoiseTheme {
        Surface {
            MissionDetailsTaskSection(
                tasks = missionFixture.tasks
            )
        }
    }
}