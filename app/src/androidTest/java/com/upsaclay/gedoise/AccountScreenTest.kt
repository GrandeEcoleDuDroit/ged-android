package com.upsaclay.gedoise

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.gedoise.presentation.profile.accountinformation.AccountInformationScreen
import com.upsaclay.gedoise.presentation.profile.accountinformation.AccountInformationScreenState
import com.upsaclay.gedoise.presentation.profile.accountinformation.AccountInformationViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AccountScreenTest {
    private val uiStateFixture = AccountInformationViewModel.AccountInformationUiState(
        user = userFixture,
        screenState = AccountInformationScreenState.READ,
        profilePictureUri = null,
        loading = false,
    )

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val viewModel: AccountInformationViewModel = mockk()

    @Before
    fun setUp() {
        every { viewModel.uiState } returns MutableStateFlow(AccountInformationViewModel.AccountInformationUiState())
        every { viewModel.event } returns MutableSharedFlow()
        every { viewModel.onProfilePictureUriChange(any()) } returns Unit
        every { viewModel.onScreenStateChange(any()) } returns Unit
        every { viewModel.deleteProfilePicture() } returns Unit
        every { viewModel.cancelEdit() } returns Unit
    }

    @Test
    fun delete_profile_picture_dialog_should_be_display_when_delete_profile_picture_button_is_clicked() {
        // When
        rule.setContent {
            AccountInformationScreen(
                user = uiStateFixture.user!!,
                loading = uiStateFixture.loading,
                screenState = uiStateFixture.screenState,
                profilePictureUri = uiStateFixture.profilePictureUri,
                onProfilePictureUriChange = viewModel::onProfilePictureUriChange,
                onScreenStateChange = viewModel::onScreenStateChange,
                onDeleteProfilePictureClick = viewModel::deleteProfilePicture,
                onSaveProfilePictureClick = viewModel::cancelEdit,
                onCancelUpdateProfilePictureClick = viewModel::cancelEdit,
                onBackClick = { }
            )
        }

        rule.onNodeWithTag(rule.activity.getString(R.string.account_screen_profile_picture_tag)).performClick()
        rule.onNodeWithTag(rule.activity.getString(R.string.account_screen_delete_profile_picture_button_tag)).performClick()

        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.account_screen_delete_profile_picture_dialog_tag)).assertExists()
    }

    @Test
    fun member_field_should_be_shown_when_user_is_member() {
        // When
        rule.setContent {
            AccountInformationScreen(
                user = uiStateFixture.user!!,
                loading = uiStateFixture.loading,
                screenState = uiStateFixture.screenState,
                profilePictureUri = uiStateFixture.profilePictureUri,
                onProfilePictureUriChange = viewModel::onProfilePictureUriChange,
                onScreenStateChange = viewModel::onScreenStateChange,
                onDeleteProfilePictureClick = viewModel::deleteProfilePicture,
                onSaveProfilePictureClick = viewModel::cancelEdit,
                onCancelUpdateProfilePictureClick = viewModel::cancelEdit,
                onBackClick = { }
            )
        }

        // Then
        rule.onNodeWithTag(rule.activity.getString(com.upsaclay.common.R.string.member_text_tag)).assertExists()
    }

    @Test
    fun member_field_should_not_be_shown_when_user_is_not_member() {
        // When
        rule.setContent {
            AccountInformationScreen(
                user = userFixture2,
                loading = uiStateFixture.loading,
                screenState = uiStateFixture.screenState,
                profilePictureUri = uiStateFixture.profilePictureUri,
                onProfilePictureUriChange = viewModel::onProfilePictureUriChange,
                onScreenStateChange = viewModel::onScreenStateChange,
                onDeleteProfilePictureClick = viewModel::deleteProfilePicture,
                onSaveProfilePictureClick = viewModel::cancelEdit,
                onCancelUpdateProfilePictureClick = viewModel::cancelEdit,
                onBackClick = { }
            )
        }

        // Then
        rule.onNodeWithTag(rule.activity.getString(com.upsaclay.common.R.string.member_text_tag)).assertDoesNotExist()
    }
}