package com.upsaclay.gedoise.viewmodel

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
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

    private val userRepository : UserRepository = mockk()
    private lateinit var viewModel : SupportContactViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)

        every { userRepository.user } returns MutableStateFlow(userFixture)
        coEvery { userRepository.getCurrentUser() } returns userFixture

        viewModel = SupportContactViewModel()
    }

    @Test
    fun send_mail_shout_send_mail(){
        viewModel.sendMail()

        coVerify { userRepository.getCurrentUser() }
    }

}