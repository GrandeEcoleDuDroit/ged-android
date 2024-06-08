package com.upsaclay.authentication

import com.upsaclay.authentication.data.AuthenticationState
import com.upsaclay.authentication.domain.IsAuthenticatedUseCase
import com.upsaclay.authentication.domain.LoginParisSaclayUseCase
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
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals

class AuthenticationViewModelTest : KoinTest {
    private val authenticationViewModel: AuthenticationViewModel by inject()
    private val loginParisSaclayUseCase: LoginParisSaclayUseCase by inject()
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase by inject()
    private val logoutUseCase: LogoutUseCase by inject()

    companion object {
        private val testModule: Module = module {
            single { AuthenticationViewModel() }
            single { mockk<LoginParisSaclayUseCase>() }
            single { mockk<IsAuthenticatedUseCase>() }
            single { mockk<LogoutUseCase>() }
        }
    }

    @Before
    fun setUp(){
        startKoin {
            modules(testModule)
        }
        coEvery {
            loginParisSaclayUseCase(any(), any())
        } returns Result.success(AuthenticationState.AUTHENTICATED)
        every { isAuthenticatedUseCase() } returns false
        every { logoutUseCase() } returns Unit
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun authentication_state_is_unauthenticated_by_default(){
        assertEquals(authenticationViewModel.authenticationState.value, AuthenticationState.UNAUTHENTICATED)
    }

    @Test
    fun authentication_state_is_authenticated_when_preference_is_true(){
        every { isAuthenticatedUseCase() } returns true
        assertEquals(authenticationViewModel.authenticationState.value, AuthenticationState.AUTHENTICATED)
    }

    @Test
    fun authentication_state_is_error_input_when_input_is_blank(){
        authenticationViewModel.loginWithParisSaclay()
        val result = authenticationViewModel.authenticationState.value
        assertEquals(result, AuthenticationState.ERROR_INPUT)
    }

    @Test
    fun authentication_state_is_unauthenticated_when_logout(){
        authenticationViewModel.logout()
        assertEquals(authenticationViewModel.authenticationState.value, AuthenticationState.UNAUTHENTICATED)
    }

    @Test
    fun authentication_state_is_authenticated_when_login_successful(){
        authenticationViewModel.updateUsername("username")
        authenticationViewModel.updatePassword("password")
        runBlocking {
            authenticationViewModel.loginWithParisSaclay()
            delay(2000)
            assertEquals(
                authenticationViewModel.authenticationState.value,
                AuthenticationState.AUTHENTICATED
            )
        }
    }
    @Test
    fun authentication_state_is_error_authentication_when_login_failed() {
        authenticationViewModel.updateUsername("username")
        authenticationViewModel.updatePassword("password")
        coEvery { loginParisSaclayUseCase(any(), any()) } returns Result.failure(Exception())
        runBlocking {
            authenticationViewModel.loginWithParisSaclay()
            delay(2000)
            assertEquals(
                authenticationViewModel.authenticationState.value,
                AuthenticationState.ERROR_AUTHENTICATION
            )
        }
    }
}
