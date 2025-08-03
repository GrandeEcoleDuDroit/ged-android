package com.upsaclay.forum

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.forum.domain.missionFixture
import com.upsaclay.forum.presentation.components.MissionCard
import org.junit.Rule
import org.junit.Test

class MissionCardTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun school_level_should_be_displayed_when_school_level_size_is_between_1_and_3() {
        rule.setContent {
            GedoiseTheme {
                MissionCard(
                    mission = missionFixture.copy(schoolLevels = listOf(SchoolLevel.GED_1)),
                    onClick = {}
                )
            }
        }

        rule
            .onNodeWithTag(rule.activity.getString(R.string.missions_card_school_level_tag))
            .assertTextContains(SchoolLevel.GED_1.toString())
    }

    @Test
    fun school_level_should_not_be_displayed_when_school_level_size_is_not_between_1_and_3() {
        rule.setContent {
            GedoiseTheme {
                MissionCard(
                    mission = missionFixture.copy(schoolLevels = listOf()),
                    onClick = {}
                )
            }
        }

        rule
            .onNodeWithTag(rule.activity.getString(R.string.missions_card_school_level_tag))
            .assertTextContains("")
    }
}