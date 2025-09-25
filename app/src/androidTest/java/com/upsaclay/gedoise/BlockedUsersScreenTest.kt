package com.upsaclay.gedoise

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.upsaclay.common.domain.userFixture
import com.upsaclay.gedoise.presentation.profile.privacy.blockedusers.BlockedUsersDestination
import com.upsaclay.gedoise.presentation.profile.privacy.blockedusers.BlockedUsersViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BlockedUsersScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val uiStateFixture = BlockedUsersViewModel.BlockedUserUiState(
        blockedUsers = emptyList()
    )
    private val viewModel: BlockedUsersViewModel = mockk()

    @Before
    fun setUp() {
        every { viewModel.uiState } returns MutableStateFlow(uiStateFixture)
        every { viewModel.event } returns MutableSharedFlow()
    }

    @Test
    fun empty_text_should_be_displayed_when_no_blocked_user() {
        // When
        rule.setContent {
            BlockedUsersDestination(
                onBackClick = {},
                onAccountClick = {},
                viewModel = viewModel
            )
        }

        // Then
        rule.onNodeWithTag(
            rule.activity.getString(R.string.empty_blocked_users_list_tag)
        ).assertExists()
    }

    @Test
    fun blocked_user_item_should_be_displayed_when_blocked_user() {
        // Given
        val blockedUser = userFixture.copy(id = "blockedUserId")
        val uiStateWithBlockedUser = uiStateFixture.copy(
            blockedUsers = listOf(blockedUser)
        )
        every { viewModel.uiState } returns MutableStateFlow(uiStateWithBlockedUser)

        // When
        rule.setContent {
            BlockedUsersDestination(
                onBackClick = {},
                onAccountClick = {},
                viewModel = viewModel
            )
        }

        // Then
        rule.onNodeWithTag(
            rule.activity.getString(R.string.blocked_user_item_tag) + blockedUser.id
        ).assertExists()
    }
}