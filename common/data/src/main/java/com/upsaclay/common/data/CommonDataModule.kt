package com.upsaclay.common.data

import com.upsaclay.common.data.local.BlockedUserLocalDataSource
import com.upsaclay.common.data.local.ImageLocalDataSource
import com.upsaclay.common.data.local.UserLocalDataSource
import com.upsaclay.common.data.local.datastore.BlockedUserDataStore
import com.upsaclay.common.data.local.datastore.UserDataStore
import com.upsaclay.common.data.remote.BlockedUserRemoteDataSource
import com.upsaclay.common.data.remote.ImageRemoteDataSource
import com.upsaclay.common.data.remote.UserRemoteDataSource
import com.upsaclay.common.data.remote.api.BlockedUserApi
import com.upsaclay.common.data.remote.api.FcmApi
import com.upsaclay.common.data.remote.api.ImageApi
import com.upsaclay.common.data.remote.api.ImageApiImpl
import com.upsaclay.common.data.remote.api.WhiteListApi
import com.upsaclay.common.data.remote.api.user.UserApi
import com.upsaclay.common.data.remote.api.user.UserApiImpl
import com.upsaclay.common.data.remote.api.user.UserFirestoreApi
import com.upsaclay.common.data.remote.api.user.UserServerApi
import com.upsaclay.common.data.repository.BlockedUserRepositoryImpl
import com.upsaclay.common.data.repository.FileRepositoryImpl
import com.upsaclay.common.data.repository.ImageRepositoryImpl
import com.upsaclay.common.data.repository.UserRepositoryImpl
import com.upsaclay.common.data.repository.WhiteListRepositoryImpl
import com.upsaclay.common.data.utils.e
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.repository.WhiteListRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val GED_SERVER_QUALIFIER = named("server_qualifier")
private val OKHTTP_CLIENT_QUALIFIER = named("okhttp_client_qualifier")
private val BACKGROUND_SCOPE = named("BackgroundScope")

val commonDataModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
            SupervisorJob() +
                Dispatchers.IO +
                CoroutineExceptionHandler { _, throwable ->
                    e("Uncaught error in backgroundScope", throwable)
                }
        )
    }

    singleOf(::AuthInterceptor) { bind<Interceptor>() }

    single<OkHttpClient>(OKHTTP_CLIENT_QUALIFIER) {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>())
            .build()
    }

    single<Retrofit>(GED_SERVER_QUALIFIER) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(get(OKHTTP_CLIENT_QUALIFIER))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(UserServerApi::class.java)
    }

    single {
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(FcmApi::class.java)
    }

    single {
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(WhiteListApi::class.java)
    }

    single {
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(BlockedUserApi::class.java)
    }

    singleOf(::ImageApiImpl) { bind<ImageApi>() }
    singleOf(::ImageRemoteDataSource)
    singleOf(::ImageLocalDataSource)
    singleOf(::ImageRepositoryImpl) { bind<ImageRepository>() }

    singleOf(::FileRepositoryImpl) { bind<FileRepository>() }

    singleOf(::UserFirestoreApi)
    singleOf(::UserApiImpl) { bind<UserApi>() }
    singleOf(::UserRemoteDataSource)
    singleOf(::UserDataStore)
    singleOf(::UserLocalDataSource)
    single<UserRepository> {
        UserRepositoryImpl(
            userRemoteDataSource = get(),
            userLocalDataSource = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }

    singleOf(::BlockedUserRemoteDataSource)
    singleOf(::BlockedUserDataStore)
    singleOf(::BlockedUserLocalDataSource)
    singleOf(::BlockedUserRepositoryImpl) { bind<BlockedUserRepository>() }

    singleOf(::WhiteListRepositoryImpl) { bind<WhiteListRepository>() }
}