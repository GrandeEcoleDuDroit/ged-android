package com.upsaclay.gedoise

import android.app.Application
import com.upsaclay.authentication.authenticationModule
import com.upsaclay.core.coreModule
import com.upsaclay.news.newsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GedoiseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@GedoiseApplication)
            modules(
                listOf(
                    appModule,
                    authenticationModule,
                    coreModule,
                    newsModule
                )
            )
        }
    }
}