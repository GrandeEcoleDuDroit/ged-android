package com.upsaclay.gedoise.usecase

import android.content.Context
import com.upsaclay.gedoise.domain.usecase.SendMailUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SendMailUseCaseTest {
    private val useCase : SendMailUseCase = mockk()

    @Before
    fun setUp(){
        coEvery { useCase(any(),any()) } returns Unit
    }


    @Test
    fun useCase_should_send_a_email(){
        val subject = "test"
        val message = "Mail test"
        useCase(subject,message)

        // que dois-je vérifier ?
    }
}