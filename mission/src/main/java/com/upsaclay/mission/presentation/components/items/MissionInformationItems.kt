package com.upsaclay.mission.presentation.components.items

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.presentation.components.TextIcon
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.presentation.MissionPresentationUtils

@Composable
fun MissionInformationValuesItem(
    modifier: Modifier = Modifier,
    mission: Mission,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    val missionInformationValues = missionInformationValues(mission)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        missionInformationValues.forEach {
            TextIcon(
                icon = {
                    Icon(
                        painter = painterResource(it.iconRes),
                        contentDescription = null
                    )
                },
                text = {
                    Text(
                        text = it.text,
                        style = textStyle
                    )
                }
            )
        }
    }
}

@Composable
private fun missionInformationValues(mission: Mission): List<MissionInformationValue> {
    val missionInformationValues = mutableListOf(
        MissionInformationValue(
            iconRes = com.upsaclay.common.R.drawable.ic_outline_calendar,
            text = MissionPresentationUtils.formatDate(mission.startDate, mission.endDate)
        ),
        MissionInformationValue(
            iconRes = R.drawable.ic_outline_school,
            text = if (mission.schoolLevelRestricted) {
                MissionPresentationUtils.formatSchoolLevels(mission.schoolLevels)
            } else {
                stringResource(R.string.everyone)
            }
        ),
        MissionInformationValue(
            iconRes = com.upsaclay.common.R.drawable.ic_outline_group,
            text = if (mission.full) {
                stringResource(R.string.full)
            } else {
                stringResource(
                    R.string.remaining_spots,
                    MissionPresentationUtils.formatRemainingParticipants(
                        participantsCount = mission.participants.size,
                        maxParticipants = mission.maxParticipants
                    )
                )
            }
        )
    )

    mission.duration?.let {
        missionInformationValues.add(
            MissionInformationValue(
                iconRes = R.drawable.ic_outline_schedule,
                text = it
            )
        )
    }

    return missionInformationValues
}

private data class MissionInformationValue(
    @DrawableRes val iconRes: Int,
    val text: String
)

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MissionInformationValuesItemPreview() {
    GedoiseTheme {
        Surface {
            MissionInformationValuesItem(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                mission = missionFixture
            )
        }
    }
}