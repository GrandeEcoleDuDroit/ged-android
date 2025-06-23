package com.upsaclay.authentication

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.upsaclay.authentication.presentation.forgopassword.ForgotPasswordViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ForgotPasswordScreenKtTest {
    private val email : String = "email@example.com"
    private val uiStateFixture = ForgotPasswordViewModel.ForgotPasswordUiState(
        email = email
    )

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val viewModel: ForgotPasswordViewModel = mockk()

    @Before
    fun setUp() {
        every { viewModel.uiState } returns MutableStateFlow(ForgotPasswordViewModel.ForgotPasswordUiState())
        every { viewModel.onEmailChange(email) } returns Unit
        every { viewModel.sendMail() } returns Unit

    }

    @Test
    fun send_button_should_be_present() {

    }

    @Test
    fun field_should_be_present() {

    }

    @Test
    fun validation_message_text_should_be_there_when_button_clicked() {

    }

    @Test
    fun inexistante_mail_message_text_should_be_there_when_button_clicked() {

    }

    @Test
    fun error_connexion_message_text_should_be_there_when_button_clicked() {

    }

    @Test
    fun blanck_field_message_text_should_be_there_when_button_clicked() {

    }
}