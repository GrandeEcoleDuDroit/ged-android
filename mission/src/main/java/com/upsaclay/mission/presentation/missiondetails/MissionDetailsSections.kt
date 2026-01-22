package com.upsaclay.mission.presentation.missiondetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallMediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.EmptyText
import com.upsaclay.common.presentation.components.SectionTitle
import com.upsaclay.common.presentation.components.UserItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.presentation.MissionPresentationUtils.MAX_USER_ITEM_DISPLAYED
import com.upsaclay.mission.presentation.MissionPresentationUtils.contentStyle
import com.upsaclay.mission.presentation.MissionPresentationUtils.descriptionStyle
import com.upsaclay.mission.presentation.MissionPresentationUtils.titleStyle
import com.upsaclay.mission.presentation.components.items.MissionInformationValuesItem
import com.upsaclay.mission.presentation.components.items.MissionUserItem

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

        MissionInformationValuesItem(
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
    onManagerClick: (User) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.managers)
            )

            if (managers.size > MAX_USER_ITEM_DISPLAYED) {
                TextButton(
                    modifier = Modifier.height(30.dp),
                    contentPadding = PaddingValues(
                        start = ButtonDefaults.TextButtonContentPadding.calculateStartPadding(
                            LayoutDirection.Ltr),
                        end = ButtonDefaults.TextButtonContentPadding.calculateEndPadding(
                            LayoutDirection.Ltr)
                    ),
                    onClick = onSeeAllClick
                ) {
                    Text(text = stringResource(R.string.see_all_users, managers.size - MAX_USER_ITEM_DISPLAYED))
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding)))

        managers.take(MAX_USER_ITEM_DISPLAYED).forEach {
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MissionDetailsParticipantSection(
    modifier: Modifier = Modifier,
    participants: List<User>,
    onParticipantClick: (User) -> Unit,
    onLongParticipantClick: (User) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.participants)
            )

            if (participants.size > MAX_USER_ITEM_DISPLAYED) {
                TextButton(
                    modifier = Modifier.height(30.dp),
                    contentPadding = PaddingValues(
                        start = ButtonDefaults.TextButtonContentPadding.calculateStartPadding(
                            LayoutDirection.Ltr),
                        end = ButtonDefaults.TextButtonContentPadding.calculateEndPadding(
                            LayoutDirection.Ltr)
                    ),
                    onClick = onSeeAllClick
                ) {
                    Text(text = stringResource(R.string.see_all_users, participants.size - MAX_USER_ITEM_DISPLAYED))
                }
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding)))

        if (participants.isEmpty()) {
            EmptyText(
                text = stringResource(R.string.no_participant),
                textStyle = contentStyle
            )

            Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding)))
        } else {
            participants.take(MAX_USER_ITEM_DISPLAYED).forEach {
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

@Composable
fun MissionDetailsTaskSection(
    modifier: Modifier = Modifier,
    missionTasks: List<MissionTask>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        SectionTitle(title = stringResource(R.string.tasks))

        Column(
            verticalArrangement = Arrangement.smallMediumSpacing()
        ) {
            missionTasks.forEach {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.smallSpacing()
                ) {
                    Text(text = "\u2022", fontSize = 20.sp)
                    Text(text = it.value, style = contentStyle)
                }
            }
        }
    }
}

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
                onManagerClick = {},
                onSeeAllClick = {}
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
                participants = emptyList(),
                onParticipantClick = {},
                onLongParticipantClick = {},
                onSeeAllClick = {}
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
                missionTasks = missionFixture.tasks
            )
        }
    }
}