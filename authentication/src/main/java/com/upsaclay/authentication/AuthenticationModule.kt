package com.upsaclay.authentication

import com.upsaclay.authentication.data.AuthenticationRepository
import com.upsaclay.authentication.data.AuthenticationRepositoryImpl
import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.remote.AuthenticationParisSaclayApi
import com.upsaclay.authentication.data.remote.AuthenticationRemoteDataSource
import com.upsaclay.authentication.domain.GenerateHashUseCase
import com.upsaclay.authentication.domain.GenerateRandomString
import com.upsaclay.authentication.domain.IsAccountExistUseCase
import com.upsaclay.authentication.domain.IsAuthenticatedUseCase
import com.upsaclay.authentication.domain.LoginUseCase
import com.upsaclay.authentication.domain.LogoutUseCase
import com.upsaclay.authentication.domain.RegistrationUseCase
import com.upsaclay.authentication.domain.StoreUserUseCase
import com.upsaclay.authentication.ui.AuthenticationViewModel
import com.upsaclay.authentication.ui.registration.RegistrationViewModel
import com.upsaclay.core.domain.SharedPreferenceUseCase
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

    singleOf(::LoginUseCase)
    singleOf(::IsAuthenticatedUseCase)
    singleOf(::LogoutUseCase)
    singleOf(::GenerateRandomString)
    singleOf(::GenerateHashUseCase)
    singleOf(::SharedPreferenceUseCase)
    singleOf(::StoreUserUseCase)
    singleOf(::IsAccountExistUseCase)
    singleOf(::RegistrationUseCase)

    singleOf(::AuthenticationRemoteDataSource)
    singleOf(::AuthenticationLocalDataSource)
    singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
}