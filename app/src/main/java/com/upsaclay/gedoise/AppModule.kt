package com.upsaclay.gedoise

import androidx.room.Room
import com.upsaclay.gedoise.data.GedoiseDatabase
import com.upsaclay.gedoise.ui.MainViewModel
import com.upsaclay.gedoise.ui.profile.ProfileViewModel
import com.upsaclay.gedoise.ui.profile.account.AccountInfoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            GedoiseDatabase::class.java, "GedoiseDatabase"
        ).build()
    }

    single { get<GedoiseDatabase>().announcementDao() }

    viewModelOf(::MainViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::AccountInfoViewModel)
}