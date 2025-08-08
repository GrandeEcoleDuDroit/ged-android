package com.upsaclay.gedoise.usecase

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.gedoise.domain.usecase.SendMailUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SendMailUseCaseTest {
    private lateinit var useCase : SendMailUseCase
    private val userRepository : UserRepository = mockk()


    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp(){
        every { userRepository.user } returns flowOf(userFixture)

        useCase = SendMailUseCase(
            userRepository
        )
    }

    @Test
    fun useCase_should_send_a_email(){
        useCase()

        // que dois-je vérifier ?
    }
}