package com.upsaclay.mission.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.extension.extraSmallSpacing
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.components.TextIcon
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.informationText
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.presentation.MissionFormatter

@Composable
fun MissionCard(
    modifier: Modifier = Modifier,
    mission: Mission,
    onClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    when (val state = mission.state) {
        is MissionState.Published -> {
            DefaultMissionCard(
                modifier = modifier,
                mission = mission,
                imageModel = state.imageUrl,
                onClick = onClick,
                onOptionClick = onOptionClick
            )
        }

        is MissionState.Publishing -> {
            PublishingMissionCard(
                modifier = modifier,
                mission = mission,
                imageModel = state.imagePath,
                onClick = onClick,
                onOptionClick = onOptionClick
            )
        }

        is MissionState.Error -> {
            ErrorMissionCard(
                modifier = modifier,
                mission = mission,
                imageModel = state.imagePath,
                onClick = onClick
            )
        }

        else -> Unit
    }
}

@Composable
private fun DefaultMissionCard(
    modifier: Modifier = Modifier,
    mission: Mission,
    imageModel: Any?,
    onClick: () -> Unit,
    onOptionClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Box {
            MissionCardImage(
                modifier = Modifier.align(Alignment.Center),
                model = imageModel,
            )

            OptionButton(
                modifier = Modifier
                    .padding(dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding))
                    .align(Alignment.TopEnd),
                onClick = onOptionClick
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            CardHeader(
                modifier = Modifier.fillMaxWidth(),
                mission = mission
            )

            CardContent(mission = mission)

            CardFooter(
                schoolLevels = mission.schoolLevels.takeIf { mission.schoolLevelRestricted }
            )
        }
    }
}

@Composable
private fun PublishingMissionCard(
    modifier: Modifier = Modifier,
    mission: Mission,
    imageModel: Any?,
    onClick: () -> Unit,
    onOptionClick: () -> Unit
) {
   DefaultMissionCard(
       modifier = modifier.alpha(0.5f),
       mission = mission,
       imageModel = imageModel,
       onClick = onClick,
       onOptionClick = onOptionClick
   )
}

@Composable
private fun CardHeader(
    modifier: Modifier = Modifier,
    mission: Mission,
) {
    Column(
        verticalArrangement = Arrangement.smallSpacing()
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.smallSpacing()
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = mission.title,
                style = titleStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            TextIcon(
                modifier = Modifier.padding(top = dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding)),
                icon = {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_group),
                        contentDescription = null
                    )
                },
                text = {
                    Text(
                        text = stringResource(
                            R.string.participant_number,
                            mission.participants.size,
                            mission.maxParticipants
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )
        }

        Text(
            text = MissionFormatter.formatDate(mission.startDate, mission.endDate),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.informationText
        )
    }
}

@Composable
private fun CardContent(
    modifier: Modifier = Modifier,
    mission: Mission
) {
    Text(
        modifier = modifier,
        text = mission.description,
        style = descriptionStyle,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun CardFooter(schoolLevels: List<SchoolLevel>?) {
    schoolLevels?.let {
        Text(
            text = MissionFormatter.formatSchoolLevels(it),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun ErrorMissionCard(
    modifier: Modifier = Modifier,
    mission: Mission,
    imageModel: Any?,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Box {
            MissionCardImage(
                modifier = Modifier.align(Alignment.Center),
                model = imageModel
            )

            ErrorBanner(modifier = Modifier.align(Alignment.TopCenter))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            CardHeader(
                modifier = Modifier.fillMaxWidth(),
                mission = mission,
            )

            CardContent(mission = mission)

            if (mission.schoolLevelRestricted) {
                CardFooter(schoolLevels = mission.schoolLevels)
            }
        }
    }
}

@Composable
private fun ErrorBanner(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
            .padding(
                vertical = dimensionResource(com.upsaclay.common.R.dimen.extra_small_padding),
                horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.extraSmallSpacing()
    ) {
        Icon(
            painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_error),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(18.dp)
        )

        Text(
            modifier = Modifier
                .padding(
                    top = dimensionResource(com.upsaclay.common.R.dimen.small_padding),
                    bottom = dimensionResource(com.upsaclay.common.R.dimen.small_padding),
                    end = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)
                ),
            text = stringResource(R.string.sending_error),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun MissionCardImage(
    modifier: Modifier = Modifier,
    model: Any?
) {
    MissionImage(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.mission_card_image_height)),
        model = model
    )
}

private val titleStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.titleLarge

private val descriptionStyle: TextStyle
    @Composable
    get() = MaterialTheme.typography.bodyLarge

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun DefaultMissionCardPreview() {
    val mission = missionFixture

    GedoiseTheme {
        Surface {
            DefaultMissionCard(
                mission = mission,
                imageModel = null,
                onClick = {},
                onOptionClick = {}
            )
        }
    }
}

@Phones
@Composable
private fun PublishingMissionCardPreview() {
    val mission = missionFixture

    GedoiseTheme {
        Surface {
            PublishingMissionCard(
                mission = mission,
                imageModel = null,
                onClick = {},
                onOptionClick = {}
            )
        }
    }
}

@Phones
@Composable
private fun ErrorMissionCardPreview() {
    val mission = missionFixture

    GedoiseTheme {
        Surface {
            ErrorMissionCard(
                mission = mission,
                imageModel = null,
                onClick = {}
            )
        }
    }
}