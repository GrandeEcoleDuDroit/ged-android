package com.upsaclay.authentication

import com.upsaclay.authentication.domain.usecase.GenerateHashUseCase
import com.upsaclay.authentication.domain.usecase.IsAccountExistUseCase
import com.upsaclay.authentication.domain.usecase.IsUserAuthenticatedUseCase
import com.upsaclay.authentication.domain.usecase.LoginUseCase
import com.upsaclay.authentication.domain.usecase.RegistrationUseCase
import com.upsaclay.authentication.presentation.AuthenticationViewModel
import com.upsaclay.authentication.presentation.registration.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authenticationModule = module {

    viewModelOf(::AuthenticationViewModel)
    viewModelOf(::RegistrationViewModel)

    singleOf(::GenerateHashUseCase)
    singleOf(::IsAccountExistUseCase)
    singleOf(::IsUserAuthenticatedUseCase)
    singleOf(::LoginUseCase)
    singleOf(::RegistrationUseCase)
}