package com.upsaclay.common

import com.upsaclay.common.data.local.UserDataStore
import com.upsaclay.common.data.local.UserLocalDataSource
import com.upsaclay.common.data.remote.ImageRemoteDataSource
import com.upsaclay.common.data.remote.UserRemoteDataSource
import com.upsaclay.common.data.remote.api.ImageRemoteApi
import com.upsaclay.common.data.remote.api.UserRemoteApi
import com.upsaclay.common.data.repository.DrawableRepositoryImpl
import com.upsaclay.common.data.repository.FileRepositoryImpl
import com.upsaclay.common.data.repository.ImageRepositoryImpl
import com.upsaclay.common.data.repository.UserRepositoryImpl
import com.upsaclay.common.domain.repository.DrawableRepository
import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.DeleteUserProfilePictureUseCase
import com.upsaclay.common.domain.usecase.DownloadImageFromOracleBucketUseCase
import com.upsaclay.common.domain.usecase.GetDrawableUriUseCase
import com.upsaclay.common.domain.usecase.GetUserUseCase
import com.upsaclay.common.domain.usecase.UpdateUserProfilePictureUseCase
import com.upsaclay.common.domain.usecase.UploadImageToOracleBucketUseCase
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SERVER_1_RETROFIT_QUALIFIER = "server_1_qualifier"
const val SERVER_2_RETROFIT_QUALIFIER = "server_2_qualifier"

private val okHttpClient = OkHttpClient.Builder().build()

val coreModule = module {

    single<Retrofit>(qualifier = named(SERVER_1_RETROFIT_QUALIFIER)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVICE_1_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<Retrofit>(qualifier = named(SERVER_2_RETROFIT_QUALIFIER)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVICE_2_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>(qualifier = named(SERVER_1_RETROFIT_QUALIFIER)).create(ImageRemoteApi::class.java)
    }

    single {
        get<Retrofit>(qualifier = named(SERVER_1_RETROFIT_QUALIFIER)).create(UserRemoteApi::class.java)
    }

    singleOf(::DrawableRepositoryImpl) { bind<DrawableRepository>() }
    singleOf(::FileRepositoryImpl) { bind<FileRepository>() }
    
    singleOf(::ImageRepositoryImpl) { bind<ImageRepository>() }
    singleOf(::ImageRemoteDataSource)

    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::UserRemoteDataSource)
    singleOf(::UserLocalDataSource)
    singleOf(::UserDataStore)

    singleOf(::DownloadImageFromOracleBucketUseCase)
    singleOf(::GetUserUseCase)
    singleOf(::GetDrawableUriUseCase)
    singleOf(::DeleteUserProfilePictureUseCase)
    singleOf(::UpdateUserProfilePictureUseCase)
    singleOf(::UploadImageToOracleBucketUseCase)
}