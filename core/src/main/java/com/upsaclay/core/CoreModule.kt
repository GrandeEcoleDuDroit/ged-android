package com.upsaclay.core

import com.upsaclay.core.domain.SharedPreferenceUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val URL_SERVER = "http://89.168.52.45:3000"

val coreModule = module {
    singleOf(::SharedPreferenceUseCase)
    single {
        Retrofit.Builder()
            .baseUrl(URL_SERVER)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}