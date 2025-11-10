package com.upsaclay.common.domain

import com.upsaclay.common.domain.usecase.DeleteProfilePictureUseCase
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.common.domain.usecase.GetUsersUseCase
import com.upsaclay.common.domain.usecase.SynchronizeBlockedUsersUseCase
import com.upsaclay.common.domain.usecase.UpdateProfilePictureUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonDomainModule = module {
    singleOf(::DeleteProfilePictureUseCase)
    singleOf(::UpdateProfilePictureUseCase)
    singleOf(::SynchronizeBlockedUsersUseCase)
    singleOf(::GetUsersUseCase)
    singleOf(::GenerateIdUseCase)
}