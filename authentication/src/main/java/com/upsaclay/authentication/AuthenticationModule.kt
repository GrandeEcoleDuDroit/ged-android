package com.upsaclay.authentication

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.remote.AuthenticationParisSaclayApi
import com.upsaclay.authentication.data.remote.AuthenticationRemoteDataSource
import com.upsaclay.authentication.data.repository.AuthenticationRepositoryImpl
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.authentication.domain.usecase.GenerateHashUseCase
import com.upsaclay.authentication.domain.usecase.GenerateRandomString
import com.upsaclay.authentication.domain.usecase.IsAccountExistUseCase
import com.upsaclay.authentication.domain.usecase.IsAuthenticatedUseCase
import com.upsaclay.authentication.domain.usecase.LoginUseCase
import com.upsaclay.authentication.domain.usecase.RegistrationUseCase
import com.upsaclay.authentication.ui.AuthenticationViewModel
import com.upsaclay.authentication.ui.registration.RegistrationViewModel
import com.upsaclay.common.domain.usecase.GetDrawableUriUseCase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val PARIS_SACLAY_SERVER_URL = "https://adonis-api.universite-paris-saclay.fr"
private const val PARIS_SACLAY_SERVER_QUALIFIER = "paris_saclay_qualifier"

val authenticationModule = module {
    single<Retrofit>(qualifier = named(PARIS_SACLAY_SERVER_QUALIFIER)) {
        Retrofit.Builder()
            .baseUrl(PARIS_SACLAY_SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>(qualifier = named(PARIS_SACLAY_SERVER_QUALIFIER))
            .create(AuthenticationParisSaclayApi::class.java)
    }

    viewModelOf(::AuthenticationViewModel)
    viewModelOf(::RegistrationViewModel)

    singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
    singleOf(::AuthenticationRemoteDataSource)
    singleOf(::AuthenticationLocalDataSource)

    singleOf(::LoginUseCase)
    singleOf(::IsAuthenticatedUseCase)
    singleOf(::GenerateRandomString)
    singleOf(::GenerateHashUseCase)
    singleOf(::GetDrawableUriUseCase)
    singleOf(::IsAccountExistUseCase)
    singleOf(::RegistrationUseCase)

}