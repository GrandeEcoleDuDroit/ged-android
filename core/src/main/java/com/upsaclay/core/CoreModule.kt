package com.upsaclay.core

import com.upsaclay.core.data.remote.ImageApi
import com.upsaclay.core.data.remote.ImageRepository
import com.upsaclay.core.data.remote.ImageRepositoryImpl
import com.upsaclay.core.data.remote.UserApi
import com.upsaclay.core.domain.CreateFileUseCase
import com.upsaclay.core.domain.DownloadImageOracleBucketUseCase
import com.upsaclay.core.domain.GetBytesFileFromUriUseCase
import com.upsaclay.core.domain.GetDrawableUriUseCase
import com.upsaclay.core.domain.SharedPreferenceUseCase
import com.upsaclay.core.domain.UploadImageOracleBucketUseCase
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
        get<Retrofit>(qualifier = named(GEDOISE_VM_1_QUALIFIER)).create(ImageApi::class.java)
    }

    single {
        get<Retrofit>(qualifier = named(GEDOISE_VM_1_QUALIFIER)).create(UserApi::class.java)
    }

    singleOf(::ImageRepositoryImpl){ bind<ImageRepository>() }
    singleOf(::SharedPreferenceUseCase)
    singleOf(::GetDrawableUriUseCase)
    singleOf(::GetBytesFileFromUriUseCase)
    singleOf(::CreateFileUseCase)
    singleOf(::UploadImageOracleBucketUseCase)
    singleOf(::DownloadImageOracleBucketUseCase)
}