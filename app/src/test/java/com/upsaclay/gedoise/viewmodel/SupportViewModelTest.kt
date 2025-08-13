package com.upsaclay.gedoise.viewmodel

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.gedoise.domain.usecase.SendMailUseCase
import com.upsaclay.gedoise.presentation.profile.supportContact.SupportContactViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SupportContactViewModelTest {

    private lateinit var viewModel : SupportContactViewModel
    private val sendMailUseCase : SendMailUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp(){
        coEvery { sendMailUseCase(any(),any()) } returns Unit
        Dispatchers.setMain(testDispatcher)
        viewModel = SupportContactViewModel(sendMailUseCase)
    }

    @Test
    fun send_mail_shout_send_mail(){
        viewModel.sendMail()
        coVerify { sendMailUseCase(any(),any()) }

    }

    @Test
    fun on_subject_change_should_update_subject(){
        viewModel.onSubjectChange("hello")
        assert(viewModel.uiState.value.subject == "hello")

    }

    @Test
    fun on_message_change_should_update_message() {
        viewModel.onMessageChange("hello")
        assert(viewModel.uiState.value.message == "hello")
    }

}