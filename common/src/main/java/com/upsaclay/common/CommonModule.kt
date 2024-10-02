package com.upsaclay.common

import com.upsaclay.common.domain.usecase.ConvertLocalDateTimeUseCase
import com.upsaclay.common.domain.usecase.ConvertTimestampUseCase
import com.upsaclay.common.domain.usecase.DeleteUserProfilePictureUseCase
import com.upsaclay.common.domain.usecase.GetAllUserUseCase
import com.upsaclay.common.domain.usecase.GetCurrentUserFlowUseCase
import com.upsaclay.common.domain.usecase.GetCurrentUserUseCase
import com.upsaclay.common.domain.usecase.GetDrawableUriUseCase
import com.upsaclay.common.domain.usecase.GetElapsedTimeUseCase
import com.upsaclay.common.domain.usecase.GetUserUseCase
import com.upsaclay.common.domain.usecase.LocalDateTimeFormatterUseCase
import com.upsaclay.common.domain.usecase.UpdateUserProfilePictureUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {

    singleOf(::ConvertLocalDateTimeUseCase)
    singleOf(::ConvertTimestampUseCase)
    singleOf(::DeleteUserProfilePictureUseCase)
    singleOf(::GetAllUserUseCase)
    singleOf(::GetCurrentUserFlowUseCase)
    singleOf(::GetCurrentUserUseCase)
    singleOf(::GetDrawableUriUseCase)
    singleOf(::GetElapsedTimeUseCase)
    singleOf(::GetUserUseCase)
    singleOf(::LocalDateTimeFormatterUseCase)
    singleOf(::UpdateUserProfilePictureUseCase)
}