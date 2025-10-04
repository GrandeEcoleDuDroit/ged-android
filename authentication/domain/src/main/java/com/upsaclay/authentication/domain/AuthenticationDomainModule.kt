package com.upsaclay.authentication.domain

import com.upsaclay.authentication.domain.usecase.ListenAuthenticationStateUseCase
import com.upsaclay.authentication.domain.usecase.LoginUseCase
import com.upsaclay.authentication.domain.usecase.RegisterUseCase
import com.upsaclay.common.domain.e
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val BACKGROUND_SCOPE = named("BackgroundScope")

val authenticationDomainModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
    SupervisorJob() +
            Dispatchers.IO +
            CoroutineExceptionHandler { _, throwable ->
                e("Uncaught error in backgroundScope", throwable)
            }
        )
    }

    singleOf(::LoginUseCase)
    singleOf(::RegisterUseCase)
    single {
        ListenAuthenticationStateUseCase(
            authenticationRepository = get(),
            userRepository = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
}