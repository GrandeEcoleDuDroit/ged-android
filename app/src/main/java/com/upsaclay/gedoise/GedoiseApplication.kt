package com.upsaclay.gedoise

import android.app.Application
import com.upsaclay.authentication.authenticationModule
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
                    authenticationModule
                )
            )
        }
    }
}