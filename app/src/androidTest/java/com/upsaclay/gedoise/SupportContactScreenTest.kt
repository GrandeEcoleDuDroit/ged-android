package com.upsaclay.gedoise

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.upsaclay.gedoise.presentation.profile.supportContact.SupportContactScreen
import com.upsaclay.gedoise.presentation.profile.supportContact.SupportContactViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SupportContactScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val uiState = SupportContactViewModel.SupportContactUiState("","")

    private val viewModel : SupportContactViewModel = mockk()

    @Before
    fun setUp() {
        every { viewModel.uiState } returns MutableStateFlow(uiState)
        every{ viewModel.onSubjectChange(any())} returns Unit
        every{ viewModel.onMessageChange(any())} returns Unit
        every{ viewModel.sendMail()} returns Unit
    }

    @Test
    fun send_button_should_send_mail(){
        rule.setContent {
            SupportContactScreen(
                onBackClick = {  },
                onObjetChange = viewModel::onSubjectChange,
                onMessageChange = viewModel::onMessageChange,
                onSendMail = viewModel::sendMail
            )
        }

        rule.onNodeWithTag(rule.activity.getString(R.string.send_support_mail_button_tag)).performClick()

        coVerify { viewModel.sendMail() }

        rule.onNodeWithTag(rule.activity.getString(R.string.send_support_mail_button_tag)).assertExists()
    }
}