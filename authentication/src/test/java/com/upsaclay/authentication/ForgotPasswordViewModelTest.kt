package com.upsaclay.authentication

import com.upsaclay.common.R as commonR
import com.upsaclay.authentication.R as authenticationR
import com.upsaclay.authentication.domain.usecase.ForgotPasswordUseCase
import com.upsaclay.authentication.presentation.forgopassword.ForgotPasswordViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ForgotPasswordViewModelTest {

    private val forgotPasswordUseCase : ForgotPasswordUseCase = mockk()
    private lateinit var forgetPasswordViewModel : ForgotPasswordViewModel
    private val email = "email@example.com"
    private val testDispatcher : TestDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        forgetPasswordViewModel = ForgotPasswordViewModel(
            forgotPasswordUseCase = forgotPasswordUseCase
        )

        coEvery { forgotPasswordUseCase(any()) } returns Unit
    }

    @Test
    fun onEmailChanged_should_be_update_email() {
        forgetPasswordViewModel.onEmailChange(email)

        Assert.assertEquals(email,forgetPasswordViewModel.uiState.value.email)

    }

    @Test
    fun sendEmail_should_be_send_a_email() {
        forgetPasswordViewModel.onEmailChange(email)
        forgetPasswordViewModel.sendMail()

        coVerify { forgotPasswordUseCase(email) }


    }

    @Test
    fun sendEmail_should_be_display_connexion_error() {
        forgetPasswordViewModel.sendMail()

        Assert.assertEquals(commonR.string.no_internet_connection,forgetPasswordViewModel.uiState.value.emailCode)
    }

    @Test
    fun sendEmail_should_be_display_inexistante_email_error() {
        forgetPasswordViewModel.sendMail()

        Assert.assertEquals(authenticationR.string.inexistante_email,forgetPasswordViewModel.uiState.value.emailCode)
    }

    @Test
    fun sendEmail_should_be_display_field_blanck_error() {
        forgetPasswordViewModel.sendMail()

        Assert.assertEquals(commonR.string.empty_field_error,forgetPasswordViewModel.uiState.value.emailCode)

    }
}