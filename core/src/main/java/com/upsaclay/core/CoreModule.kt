package com.upsaclay.core

import com.upsaclay.core.data.local.UserLocalDataSource
import com.upsaclay.core.data.remote.UserRemoteDataSource
import com.upsaclay.core.data.remote.api.ImageRemoteApi
import com.upsaclay.core.data.remote.api.UserRemoteApi
import com.upsaclay.core.data.repository.DrawableRepositoryImpl
import com.upsaclay.core.data.repository.ImageRepositoryImpl
import com.upsaclay.core.data.repository.UserRepositoryImpl
import com.upsaclay.core.domain.repository.DrawableRepository
import com.upsaclay.core.domain.repository.ImageRepository
import com.upsaclay.core.domain.repository.UserRepository
import com.upsaclay.core.domain.usecase.CreateFileUseCase
import com.upsaclay.core.domain.usecase.DownloadImageOracleBucketUseCase
import com.upsaclay.core.domain.usecase.GetFileBytesFromUriUseCase
import com.upsaclay.core.domain.usecase.UploadImageOracleBucketUseCase
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val GEDOISE_VM_1_URL = "http://89.168.52.45:3000"
private const val GEDOISE_VM_2_URL = "http://89.168.63.192:3000"
const val GEDOISE_VM_1_QUALIFIER = "gedoise-vm-1_qualifier"
const val GEDOISE_VM_2_QUALIFIER = "gedoise-vm-2_qualifier"

private val okHttpClient = OkHttpClient.Builder().build()

val coreModule = module {

    single<Retrofit>(qualifier = named(GEDOISE_VM_1_QUALIFIER)) {
        Retrofit.Builder()
            .baseUrl(GEDOISE_VM_1_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<Retrofit>(qualifier = named(GEDOISE_VM_2_QUALIFIER)) {
        Retrofit.Builder()
            .baseUrl(GEDOISE_VM_2_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>(qualifier = named(GEDOISE_VM_1_QUALIFIER)).create(ImageRemoteApi::class.java)
    }

    single {
        get<Retrofit>(qualifier = named(GEDOISE_VM_1_QUALIFIER)).create(UserRemoteApi::class.java)
    }

    singleOf(::ImageRepositoryImpl) { bind<ImageRepository>() }
    singleOf(::DrawableRepositoryImpl) { bind<DrawableRepository>() }

    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::UserRemoteDataSource)
    singleOf(::UserLocalDataSource)

    singleOf(::GetFileBytesFromUriUseCase)
    singleOf(::CreateFileUseCase)
    singleOf(::UploadImageOracleBucketUseCase)
    singleOf(::DownloadImageOracleBucketUseCase)
}