package com.upsaclay.app.domain

import com.upsaclay.app.domain.usecase.ClearDataUseCase
import com.upsaclay.app.domain.usecase.DeleteAccountUseCase
import com.upsaclay.app.domain.usecase.FcmTokenUseCase
import com.upsaclay.app.domain.usecase.ListenBlockedUserEventsUseCase
import com.upsaclay.app.domain.usecase.ListenRemoteUserUseCase
import com.upsaclay.app.domain.usecase.SynchronizeDataUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appDomainModule = module {
    singleOf(::ClearDataUseCase)
    singleOf(::DeleteAccountUseCase)
    singleOf(::SynchronizeDataUseCase)
    singleOf(::DeleteAccountUseCase)
    singleOf(::FcmTokenUseCase)
    singleOf(::ListenRemoteUserUseCase)
    singleOf(::ListenBlockedUserEventsUseCase)
}