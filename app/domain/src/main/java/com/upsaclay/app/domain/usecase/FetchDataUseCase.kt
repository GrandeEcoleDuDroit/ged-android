package com.upsaclay.app.domain.usecase

import com.upsaclay.common.domain.usecase.FetchBlockedUsersUseCase
import com.upsaclay.common.domain.usecase.FetchCurrentUserUseCase
import com.upsaclay.mission.domain.usecase.FetchMissionsUseCase
import com.upsaclay.news.domain.usecase.FetchAnnouncementsUseCase

class FetchDataUseCase(
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val fetchBlockedUsersUseCase: FetchBlockedUsersUseCase,
    private val fetchAnnouncementsUseCase: FetchAnnouncementsUseCase,
    private val fetchMissionsUseCase: FetchMissionsUseCase
) {
    suspend operator fun invoke(userId: String) {
        fetchCurrentUserUseCase(userId)
        fetchBlockedUsersUseCase(userId)
        fetchAnnouncementsUseCase()
        fetchMissionsUseCase()
    }
}