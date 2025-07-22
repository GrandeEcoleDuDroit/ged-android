package com.upsaclay.forum.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.LargeAsyncImage
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.R
import com.upsaclay.forum.domain.entity.Mission
import com.upsaclay.forum.domain.missionFixture

@Composable
fun MissionCard(
    modifier: Modifier = Modifier,
    mission: Mission,
    onClick: () -> Unit
) {
    OutlinedCard(modifier = modifier) {
        Column {
            CardHeader(imageUrl = mission.imageUrl)

            CardBody(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.small
                    ),
                mission = mission,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun CardHeader(
    modifier: Modifier = Modifier,
    imageUrl: String?
) {
    val errorPainter = painterResource(R.drawable.ic_default_mission_image)
    var isError by remember { mutableStateOf(false) }
    val defaultImageModifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.secondaryContainer)
        .padding(MaterialTheme.spacing.medium)

    imageUrl?.let {
        LargeAsyncImage(
            modifier = if (isError) defaultImageModifier else modifier.fillMaxWidth(),
            model = it,
            onError = {
                isError = true
                errorPainter
            }
        )
    } ?: run {
        Image(
            painter = painterResource(R.drawable.ic_default_mission_image),
            contentDescription = null,
            modifier = defaultImageModifier
        )
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
        CardBodyTopSection(
            title = mission.title,
            participantCount = mission.participants.size,
            participantMax = mission.participantMax
        )

        CardBodyMiddleSection(description = mission.description)

        CardBodyBottomSection(
            schoolLevels = mission.schoolLevels,
            onClick = onClick
        )
    }
}

@Composable
private fun CardBodyTopSection(
    modifier: Modifier = Modifier,
    title: String,
    participantCount: Int,
    participantMax: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        ParticipantIcon(
            participantCount = participantCount,
            participantMax = participantMax
        )
    }
}

@Composable
private fun CardBodyMiddleSection(
    modifier: Modifier = Modifier,
    description: String
) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier,
        maxLines = 4,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun CardBodyBottomSection(
    modifier: Modifier = Modifier,
    schoolLevels: List<SchoolLevel>,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = formatSchoolLevels(schoolLevels),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .weight(1f)
                .testTag(stringResource(R.string.missions_card_school_level_tag))
        )

        PrimaryButton(
            text = stringResource(com.upsaclay.common.R.string.see),
            onClick = onClick,
        )
    }
}

@Composable
private fun ParticipantIcon(
    modifier: Modifier = Modifier,
    participantCount: Int,
    participantMax: Int
) {
    Row(
        horizontalArrangement = Arrangement.smallSpacing(),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(com.upsaclay.common.R.drawable.ic_fill_group),
            contentDescription = null
        )

        Text(
            text = stringResource(R.string.participants_amount, participantCount, participantMax),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun formatSchoolLevels(schoolLevels: List<SchoolLevel>): String =
    schoolLevels
        .takeIf { it.size in 1..3 }
        ?.joinToString(separator = " - ") ?: ""

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun MissionCardPreview() {
    GedoiseTheme {
        Surface {
            MissionCard(
                modifier = Modifier.padding(MaterialTheme.spacing.medium),
                mission = missionFixture,
                onClick = {}
            )
        }
    }
}