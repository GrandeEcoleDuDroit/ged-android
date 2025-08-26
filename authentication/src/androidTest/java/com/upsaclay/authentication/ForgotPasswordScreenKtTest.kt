package com.upsaclay.authentication

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.upsaclay.common.R as commonR
import com.upsaclay.authentication.R as authenticationR
import com.upsaclay.authentication.presentation.forgopassword.ForgotPasswordScreen
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
        every { viewModel.resetPassword() } returns Unit
        rule.setContent {
            ForgotPasswordScreen(
                email = uiStateFixture.email,
                onBackClick = {},
                onResetPasswordClick = viewModel::resetPassword,
                onValueChange = viewModel::onEmailChange
            )
        }
    }

    @Test
    fun send_button_should_be_present() {

        val button : SemanticsNodeInteraction = rule.onNodeWithTag(rule.activity.getString(authenticationR.string.forgot_password_screen_next_button_tag))

        button.performClick()

        button.assertExists()

    }


    @Test
    fun field_should_be_present() {

        val field : SemanticsNodeInteraction = rule.onNodeWithTag(rule.activity.getString(authenticationR.string.forgot_password_screen_email_input_tag))

        field.assertExists()

    }

    @Test
    fun validation_message_text_should_be_there_when_button_clicked() {


        val button : SemanticsNodeInteraction = rule.onNodeWithTag(rule.activity.getString(authenticationR.string.forgot_password_screen_next_button_tag))

        button.performClick()

        val texteMessage : SemanticsNodeInteraction = rule.onNodeWithText(rule.activity.getString(authenticationR.string.validation_message))
        texteMessage.assertExists()



    }

    @Test
    fun inexistante_mail_message_text_should_be_there_when_button_clicked() {

        val button : SemanticsNodeInteraction = rule.onNodeWithTag(rule.activity.getString(authenticationR.string.forgot_password_screen_next_button_tag))

        button.performClick()

        val textMessage: SemanticsNodeInteraction = rule.onNodeWithText(rule.activity.getString(authenticationR.string.inexistante_email))

        textMessage.assertExists()

    }

    @Test
    fun error_connexion_message_text_should_be_there_when_button_clicked() {


        val textMessage: SemanticsNodeInteraction = rule.onNodeWithText(rule.activity.getString(commonR.string.no_internet_connection))

        textMessage.assertExists()

        TODO("executer une erreur de connexion")

    }

    @Test
    fun blanck_field_message_text_should_be_there_when_button_clicked() {

        val button : SemanticsNodeInteraction = rule.onNodeWithTag(rule.activity.getString(authenticationR.string.forgot_password_screen_next_button_tag))

        button.performClick()

        val textMessage: SemanticsNodeInteraction = rule.onNodeWithText(rule.activity.getString(commonR.string.empty_field_error))

        textMessage.assertExists()

    }
}