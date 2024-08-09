package com.upsaclay.gedoise

import com.upsaclay.gedoise.ui.MainViewModel
import com.upsaclay.gedoise.ui.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::ProfileViewModel)
}