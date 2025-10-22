package com.upsaclay.gedoise

import MainViewModel
import com.upsaclay.common.ConnectivityObserverImpl
import com.upsaclay.common.IntentHelper
import com.upsaclay.common.data.e
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.gedoise.presentation.navigation.NavigationViewModel
import com.upsaclay.gedoise.presentation.profile.ProfileViewModel
import com.upsaclay.gedoise.presentation.profile.account.deleteaccount.DeleteAccountViewModel
import com.upsaclay.gedoise.presentation.profile.accountinformation.AccountInformationViewModel
import com.upsaclay.gedoise.presentation.profile.blockedusers.BlockedUsersViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val BACKGROUND_SCOPE = named("BackgroundScope")

val appModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
    SupervisorJob() +
            Dispatchers.IO +
            CoroutineExceptionHandler { _, throwable ->
                e("Uncaught error in backgroundScope", throwable)
            }
        )
    }

    single<ConnectivityObserver> {
        ConnectivityObserverImpl(
            context = androidContext(),
            scope = get(BACKGROUND_SCOPE)
        )
    }

    viewModelOf(::NavigationViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::AccountInformationViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::BlockedUsersViewModel)
    viewModelOf(::DeleteAccountViewModel)

    singleOf(::IntentHelperImpl) { bind<IntentHelper>() }
}