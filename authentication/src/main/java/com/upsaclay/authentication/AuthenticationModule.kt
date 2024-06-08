package com.upsaclay.authentication

import com.upsaclay.authentication.data.AuthenticationRepository
import com.upsaclay.authentication.data.AuthenticationRepositoryImpl
import com.upsaclay.authentication.domain.GenerateHashUseCase
import com.upsaclay.authentication.domain.GenerateRandomString
import com.upsaclay.authentication.domain.IsAuthenticatedUseCase
import com.upsaclay.authentication.domain.LoginParisSaclayUseCase
import com.upsaclay.authentication.domain.LogoutUseCase
import com.upsaclay.authentication.ui.AuthenticationViewModel
import com.upsaclay.core.domain.SharedPreferenceUseCase
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authenticationModule = module {
    singleOf(::AuthenticationViewModel)

    singleOf(::LoginParisSaclayUseCase)
    singleOf(::IsAuthenticatedUseCase)
    singleOf(::LogoutUseCase)
    singleOf(::GenerateRandomString)
    singleOf(::GenerateHashUseCase)
    singleOf(::SharedPreferenceUseCase)

    singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
}