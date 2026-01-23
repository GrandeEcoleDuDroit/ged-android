package com.upsaclay.gedoise

import com.upsaclay.common.ConnectivityObserverImpl
import com.upsaclay.common.IntentHelper
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.gedoise.presentation.MainViewModel
import com.upsaclay.gedoise.presentation.navigation.NavigationViewModel
import com.upsaclay.gedoise.presentation.notification.NotificationMediator
import com.upsaclay.gedoise.presentation.profile.ProfileViewModel
import com.upsaclay.gedoise.presentation.profile.account.deleteaccount.DeleteAccountViewModel
import com.upsaclay.gedoise.presentation.profile.accountinformation.AccountInformationViewModel
import com.upsaclay.gedoise.presentation.profile.blockedusers.BlockedUsersViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<ConnectivityObserver> {
        ConnectivityObserverImpl(context = androidContext())
    }

    viewModelOf(::NavigationViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::AccountInformationViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::BlockedUsersViewModel)
    viewModelOf(::DeleteAccountViewModel)

    singleOf(::IntentHelperImpl) { bind<IntentHelper>() }

    singleOf(::NotificationMediator)
}