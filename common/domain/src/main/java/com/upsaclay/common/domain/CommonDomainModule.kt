package com.upsaclay.common.domain

import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.common.domain.usecase.GetUsersUseCase
import com.upsaclay.common.domain.usecase.NavigationRequestUseCase
import com.upsaclay.common.domain.usecase.FetchBlockedUsersUseCase
import com.upsaclay.common.domain.usecase.UpdateProfilePictureUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonDomainModule = module {
    singleOf(::UpdateProfilePictureUseCase)
    singleOf(::FetchBlockedUsersUseCase)
    singleOf(::GetUsersUseCase)
    singleOf(::GenerateIdUseCase)
    singleOf(::NavigationRequestUseCase)
}