package com.upsaclay.forum

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.forum.domain.missionsFixture
import com.upsaclay.forum.presentation.MissionFeed
import org.junit.Rule
import org.junit.Test

class MissionFeedTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun empty_mission_text_should_appear_when_missions_is_empty() {
        rule.setContent {
            GedoiseTheme {
                MissionFeed(
                    missions = emptyList(),
                    onClick = {}
                )
            }
        }

        rule
            .onNodeWithTag(rule.activity.getString(R.string.no_mission_tag))
            .assertExists()
    }

    @Test
    fun mission_card_should_be_displayed_when_missions_is_not_empty() {
        rule.setContent {
            GedoiseTheme {
                MissionFeed(
                    missions = missionsFixture,
                    onClick = {}
                )
            }
        }

        rule
            .onNodeWithTag(rule.activity.getString(R.string.missions_card_tag) + missionsFixture.first().id)
            .assertExists()
    }
}