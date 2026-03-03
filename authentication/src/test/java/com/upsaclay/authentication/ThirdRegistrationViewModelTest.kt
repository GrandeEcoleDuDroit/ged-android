package com.upsaclay.authentication

import com.upsaclay.authentication.domain.usecase.RegisterUseCase
import com.upsaclay.authentication.presentation.registration.thirdregistration.ThirdRegistrationViewModel
import com.upsaclay.common.domain.entity.SchoolLevel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ThirdRegistrationViewModelTest {
    private val registerUseCase: RegisterUseCase = mockk()

    private lateinit var viewModel: ThirdRegistrationViewModel
    
    private val testDispatcher = UnconfinedTestDispatcher()
    private val firstName = "John"
    private val lastName = "Doe"
    private val schoolLevel = SchoolLevel.LEVEL_1
    private val email = "email@example.com"
    private val password = "password1234"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery { registerUseCase.execute(any(), any(), any(), any(), any()) } returns Unit

        viewModel = ThirdRegistrationViewModel(
            registerUseCase = registerUseCase
        )
    }

    @Test
    fun onEmailChange_should_update_email() {
        // When
        viewModel.onEmailChange(email)

        // Then
        assertEquals(email, viewModel.uiState.value.email)
    }

    @Test
    fun onPasswordChange_should_update_password() {
        // When
        viewModel.onPasswordChange(password)

        // Then
        assertEquals(password, viewModel.uiState.value.password)
    }

    @Test
    fun register_should_register_user() = runTest {
        // Given
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onLegalNoticeCheckedChange(true)

        // When
        viewModel.register(firstName, lastName, schoolLevel)

        // Then
        coVerify { registerUseCase.execute(email, password, firstName, lastName, schoolLevel) }
    }

    @Test
    fun register_should_set_email_error_when_email_is_empty() {
        // Given
        viewModel.onEmailChange("")
        viewModel.onPasswordChange(password)

        // When
        viewModel.register(firstName, lastName, schoolLevel)

        // Then
        assertNotNull(viewModel.uiState.value.email)
    }

    @Test
    fun register_should_set_email_error_when_email_format_is_incorrect() {
        // Given
        viewModel.onEmailChange("email")
        viewModel.onPasswordChange(password)

        // When
        viewModel.register(firstName, lastName, schoolLevel)

        // Then
        assertNotNull(viewModel.uiState.value.email)
    }

    @Test
    fun register_should_set_password_error_when_password_is_empty() {
        // Given
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange("")

        // When
        viewModel.register(firstName, lastName, schoolLevel)

        // Then
        assertNotNull(viewModel.uiState.value.password)
    }

    @Test
    fun register_should_set_error_message_when_legal_notice_is_not_checked() {
        // Given
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onLegalNoticeCheckedChange(false)

        // When
        viewModel.register(firstName, lastName, schoolLevel)

        // Then
        assertNotNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun validateInputs_should_return_false_when_password_length_is_shorter_than_8() {
        // Given
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange("pass")

        // When
        viewModel.register(firstName, lastName, schoolLevel)

        // Then
        assertNotNull(viewModel.uiState.value.password)
    }
}