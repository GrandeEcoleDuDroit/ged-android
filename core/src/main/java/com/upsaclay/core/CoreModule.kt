package com.upsaclay.core

import com.upsaclay.core.domain.SharedPreferenceUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::SharedPreferenceUseCase)
}