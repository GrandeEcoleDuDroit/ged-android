package com.upsaclay.mission.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.theme.GedoiseTheme
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
    onClick: () -> Unit
) {
    when (val state = mission.state) {
        is MissionState.Published -> {
            DefaultMissionCard(
                modifier = modifier,
                mission = mission,
                imageModel = state.imageUrl,
                onClick = onClick
            )
        }

        is MissionState.Publishing -> {
            PublishingMissionCard(
                modifier = modifier,
                mission = mission,
                imageModel = state.imagePath,
                onClick = onClick
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
    onClick: () -> Unit
) {
    OutlinedCard(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            MissionImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    ),
                model = imageModel
            )

            CardBody(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
                        vertical = dimensionResource(com.upsaclay.common.R.dimen.small_padding)
                    ),
                mission = mission,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun PublishingMissionCard(
    modifier: Modifier = Modifier,
    mission: Mission,
    imageModel: Any?,
    onClick: () -> Unit
) {
   DefaultMissionCard(
       modifier = modifier.alpha(0.5f),
       mission = mission,
         imageModel = imageModel,
       onClick = onClick
   )
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
        Column(
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            MissionImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 16.dp,
                            bottomEnd = 16.dp
                        )
                    ),
                model = imageModel
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
                        vertical = dimensionResource(com.upsaclay.common.R.dimen.small_padding)
                    ),
                verticalArrangement = Arrangement.mediumSpacing()
            ) {
                CardBodyHeader(
                    modifier = Modifier.fillMaxWidth(),
                    mission = mission
                )

                CardBodyContent(mission = mission)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.smallSpacing()
                    ) {
                        Icon(
                            painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_error),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )

                        Text(
                            text = stringResource(R.string.sending_error),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    CardBodyFooter(
                        schoolLevels = mission.schoolLevels,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
private fun CardBody(
    modifier: Modifier = Modifier,
    mission: Mission,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        CardBodyHeader(
            modifier = Modifier.fillMaxWidth(),
            mission = mission
        )

        CardBodyContent(mission = mission)

        CardBodyFooter(
            modifier = Modifier.fillMaxWidth(),
            schoolLevels = mission.schoolLevels,
            onClick = onClick
        )
    }
}

@Composable
private fun CardBodyHeader(
    modifier: Modifier = Modifier,
    mission: Mission
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = mission.title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.smallSpacing(),
        ) {
            Icon(
                modifier = Modifier.scale(0.9f),
                painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_group),
                contentDescription = null
            )

            Text(
                text = stringResource(
                    R.string.participant_number,
                    mission.participants.size,
                    mission.maxParticipants
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun CardBodyContent(
    modifier: Modifier = Modifier,
    mission: Mission
) {
    Text(
        modifier = modifier,
        text = mission.description,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun CardBodyFooter(
    modifier: Modifier = Modifier,
    schoolLevels: List<SchoolLevel>,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = MissionFormatter.formatSchoolLevels(schoolLevels, ""),
            style = MaterialTheme.typography.bodySmall
        )

        PrimaryButton(
            text = stringResource(com.upsaclay.common.R.string.see),
            onClick = onClick,
        )
    }
}

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
        DefaultMissionCard(
            mission = mission,
            imageModel = null,
            onClick = {}
        )
    }
}

@Phones
@Composable
private fun PublishingMissionCardPreview() {
    val mission = missionFixture

    GedoiseTheme {
        PublishingMissionCard(
            mission = mission,
            imageModel = null,
            onClick = {}
        )
    }
}

@Phones
@Composable
private fun ErrorMissionCardPreview() {
    val mission = missionFixture

    GedoiseTheme {
        ErrorMissionCard(
            mission = mission,
            imageModel = null,
            onClick = {}
        )
    }
}