package com.upsaclay.mission.presentation.components.item

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.extension.smallMediumSpacing
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.presentation.MissionFormatter

@Composable
fun MissionInfoItem(
    modifier: Modifier = Modifier,
    mission: Mission
) {
    val missionInfos = listOf(
        MissionInfo(
            iconRes = com.upsaclay.common.R.drawable.ic_outline_group,
            value = stringResource(
                R.string.spot_remaining,
                MissionFormatter.formatRemainingParticipants(
                    participantsCount = mission.participants.size,
                    maxParticipants = mission.maxParticipants
                )
            )
        ),
        MissionInfo(
            iconRes = com.upsaclay.common.R.drawable.ic_outline_calendar,
            value = MissionFormatter.formatDate(mission.startDate, mission.endDate)
        ),
        MissionInfo(
            iconRes = R.drawable.ic_outline_school,
            value = MissionFormatter.formatSchoolLevels(
                schoolLevels = mission.schoolLevels,
                emptyText = stringResource(id = R.string.everyone)
            )
        )
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallMediumSpacing()
    ) {
        missionInfos.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.smallSpacing()
            ) {
                Icon(
                    painter = painterResource(it.iconRes),
                    contentDescription = null
                )

                Text(
                    text = it.value,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

data class MissionInfo(
    @DrawableRes val iconRes: Int,
    val value: String
)

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MissionInfoItemPreview() {
    GedoiseTheme {
        MissionInfoItem(
            modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            mission = missionFixture
        )
    }
}