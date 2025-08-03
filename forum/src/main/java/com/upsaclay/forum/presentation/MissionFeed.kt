package com.upsaclay.forum.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.upsaclay.common.extension.mediumLargeSpacing
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.R
import com.upsaclay.forum.domain.entity.Mission
import com.upsaclay.forum.domain.missionsFixture
import com.upsaclay.forum.presentation.components.MissionCard

@Composable
fun MissionFeed(
    modifier: Modifier = Modifier,
    missions: List<Mission>,
    onClick: (Mission) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.mediumLargeSpacing()
    ) {
        if (missions.isEmpty()) {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(stringResource(R.string.no_mission_tag)),
                    text = stringResource(id = R.string.no_mission),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.previewText,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            items(missions) {
                MissionCard(
                    modifier = Modifier.testTag(stringResource(R.string.missions_card_tag) + it.id),
                    mission = it,
                    onClick = { onClick(it) }
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
private fun MissionFeedPreview() {
    GedoiseTheme {
        Surface {
            MissionFeed(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                missions = missionsFixture,
                onClick = {}
            )
        }
    }
}