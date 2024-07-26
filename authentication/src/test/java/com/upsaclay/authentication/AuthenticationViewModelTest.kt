package com.upsaclay.authentication

import com.upsaclay.authentication.data.model.AuthenticationState
import com.upsaclay.authentication.domain.IsAuthenticatedUseCase
import com.upsaclay.authentication.domain.LoginUseCase
import com.upsaclay.authentication.domain.LogoutUseCase
import com.upsaclay.authentication.ui.AuthenticationViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals

class AuthenticationViewModelTest : KoinTest {
    private val authenticationViewModel: AuthenticationViewModel by inject()
    private val loginUseCase: LoginUseCase by inject()
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase by inject()
    private val logoutUseCase: LogoutUseCase by inject()

    companion object {
        private val testModule: Module = module {
            singleOf(::AuthenticationViewModel)
            single { mockk<LoginUseCase>() }
            single { mockk<IsAuthenticatedUseCase>() }
            single { mockk<LogoutUseCase>() }
        }
    }

    @Before
    fun setUp() {
        startKoin {
            modules(testModule)
        }
        coEvery {
            loginUseCase(any(), any())
        } returns Result.success(AuthenticationState.AUTHENTICATED)
        every { isAuthenticatedUseCase() } returns false
        every { logoutUseCase() } returns Unit
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun authentication_state_is_unauthenticated_by_default() {
        assertEquals(
            authenticationViewModel.authenticationState.value,
            AuthenticationState.UNAUTHENTICATED
        )
    }

    @Test
    fun authentication_state_is_authenticated_when_preference_is_true() {
        every { isAuthenticatedUseCase() } returns true
        assertEquals(
            authenticationViewModel.authenticationState.value,
            AuthenticationState.AUTHENTICATED
        )
    }

    @Test
    fun authentication_state_is_error_input_when_input_is_blank() {
        authenticationViewModel.login()
        val result = authenticationViewModel.authenticationState.value
        assertEquals(result, AuthenticationState.ERROR_INPUT)
    }

    @Test
    fun authentication_state_is_authenticated_when_login_successful() {
        authenticationViewModel.updateMailText("username")
        authenticationViewModel.updatePasswordText("password")
        runBlocking {
            authenticationViewModel.login()
            delay(2000)
            assertEquals(
                authenticationViewModel.authenticationState.value,
                AuthenticationState.AUTHENTICATED
            )
        }
    }

    @Test
    fun authentication_state_is_error_authentication_when_login_failed() {
        authenticationViewModel.updateMailText("username")
        authenticationViewModel.updatePasswordText("password")
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Exception())
        runBlocking {
            authenticationViewModel.login()
            delay(2000)
            assertEquals(
                authenticationViewModel.authenticationState.value,
                AuthenticationState.ERROR_AUTHENTICATION
            )
        }
    }
}
