package com.upsaclay.gedoise.viewmodel

import com.upsaclay.app.domain.notificationPreferencesFixture
import com.upsaclay.app.domain.repository.PreferencesRepository
import com.upsaclay.app.domain.usecase.ClearDataUseCase
import com.upsaclay.app.domain.usecase.FcmTokenUseCase
import com.upsaclay.app.domain.usecase.FetchDataUseCase
import com.upsaclay.app.domain.usecase.ListenDataUseCase
import com.upsaclay.authentication.domain.entity.AuthenticationState
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.gedoise.presentation.MainViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val preferencesRepository: PreferencesRepository = mockk()
    private val clearDataUseCase: ClearDataUseCase = mockk()
    private val fetchDataUseCase: FetchDataUseCase = mockk()
    private val fcmTokenUseCase: FcmTokenUseCase = mockk()
    private val listenDataUseCase: ListenDataUseCase = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var viewModel: MainViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val userId = "userId1234"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { authenticationRepository.authenticationState } returns flowOf(AuthenticationState.Authenticated(userId))
        every { connectivityObserver.connected } returns flowOf(true)
        coEvery { authenticationRepository.refreshTokenIfNecessary() } returns Unit
        coEvery { clearDataUseCase.execute() } returns Unit
        coEvery { listenDataUseCase.start(any(), any()) } returns Unit
        coEvery { listenDataUseCase.stop() } returns Unit
        coEvery { fetchDataUseCase.execute(any()) } returns Unit
        coEvery { preferencesRepository.getNotificationPreferences() } returns notificationPreferencesFixture
        coEvery { preferencesRepository.storeNotificationPreferences(any()) } returns Unit

        viewModel = MainViewModel(
            authenticationRepository = authenticationRepository,
            preferencesRepository = preferencesRepository,
            clearDataUseCase = clearDataUseCase,
            fetchDataUseCase = fetchDataUseCase,
            fcmTokenUseCase = fcmTokenUseCase,
            listenDataUseCase = listenDataUseCase,
            connectivityObserver = connectivityObserver
        )
    }

    @Test
    fun data_should_be_listened_when_user_is_authenticated() = runTest {
        // When
        viewModel.startAppDataUpdating()

        // Then
        coVerify { listenDataUseCase.start(any(), any()) }
    }

    @Test
    fun data_should_be_fetched_when_user_is_authenticated() {
        // When
        viewModel.startAppDataUpdating()

        // Then
        coVerify { fetchDataUseCase.execute(userId) }
    }

    @Test
    fun data_should_stop_be_listened_when_user_is_unauthenticated() {
        // Given
        every { authenticationRepository.authenticationState } returns flowOf(AuthenticationState.Unauthenticated)

        // When
        viewModel.startAppDataUpdating()

        // Then
        coVerify { listenDataUseCase.stop() }
    }

    @Test
    fun data_should_be_deleted_when_user_is_unauthenticated() = runTest {
        // Given
        every { authenticationRepository.authenticationState } returns flowOf(AuthenticationState.Unauthenticated)

        // When
        viewModel.startAppDataUpdating()
        advanceTimeBy(2000)
        advanceUntilIdle()

        // Then
        coVerify { clearDataUseCase.execute() }
    }

    @Test
    fun getNotificationPreferences_should_return_notificationPreferences() = runTest {
        // When
        val result = viewModel.getNotificationPreferences()

        // Then
        assert(result == notificationPreferencesFixture)
    }

    @Test
    fun storeNotificationPreferences_should_call_preferencesRepository() = runTest {
        // When
        viewModel.storeNotificationPreferences(notificationPreferencesFixture)

        // Then
        coVerify { preferencesRepository.storeNotificationPreferences(notificationPreferencesFixture) }
    }
}