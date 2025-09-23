package com.upsaclay.common

import com.upsaclay.common.presentation.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    viewModel { (userId: String) ->
        UserViewModel(
            userId = userId,
            userRepository = get(),
            blockedUserRepository = get()
        )
    }
}