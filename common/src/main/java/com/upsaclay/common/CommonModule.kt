package com.upsaclay.common

import com.upsaclay.common.presentation.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val commonModule = module {
    viewModelOf(::UserViewModel)
}