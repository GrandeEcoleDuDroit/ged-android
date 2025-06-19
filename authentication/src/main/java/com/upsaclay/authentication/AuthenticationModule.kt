package com.upsaclay.authentication

import com.upsaclay.authentication.presentation.authentication.AuthenticationViewModel
import com.upsaclay.authentication.presentation.registration.firstregistration.FirstRegistrationViewModel
import com.upsaclay.authentication.presentation.registration.secondregistration.SecondRegistrationViewModel
import com.upsaclay.authentication.presentation.registration.thirdregistration.ThirdRegistrationViewModel
import com.upsaclay.authentication.presentation.forgopassword.ForgotPasswordViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authenticationModule = module {
    viewModelOf(::AuthenticationViewModel)
    viewModelOf(::FirstRegistrationViewModel)
    viewModelOf(::SecondRegistrationViewModel)
    viewModelOf(::ThirdRegistrationViewModel)
    viewModelOf(::ForgotPasswordViewModel)
}