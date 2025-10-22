package com.upsaclay.app.domain

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appDomainModule = module {
    singleOf(::ClearDataUseCase)
    singleOf(::DeleteAccountUseCase)
    singleOf(::SynchronizeDataUseCase)
    singleOf(::DeleteAccountUseCase)
    singleOf(::FcmTokenUseCase)
    singleOf(::ListenRemoteUserUseCase)
    singleOf(::ListenBlockedUserEvents)
}