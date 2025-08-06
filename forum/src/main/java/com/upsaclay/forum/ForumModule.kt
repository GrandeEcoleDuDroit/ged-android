package com.upsaclay.forum

import com.upsaclay.forum.presentation.ForumViewModel
import com.upsaclay.forum.presentation.createmission.CreateMissionViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val forumModule = module {
    viewModelOf(::ForumViewModel)
    viewModelOf(::CreateMissionViewModel)
}