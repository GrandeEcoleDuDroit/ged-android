package com.upsaclay.app.domain.usecase

import com.upsaclay.common.domain.usecase.FetchBlockedUsersUseCase
import com.upsaclay.common.domain.usecase.FetchCurrentUserUseCase
import com.upsaclay.mission.domain.usecase.FetchMissionsUseCase
import com.upsaclay.news.domain.announcement.usecase.FetchAnnouncementsUseCase

class FetchDataUseCase(
    private val fetchCurrentUserUseCase: FetchCurrentUserUseCase,
    private val fetchBlockedUsersUseCase: FetchBlockedUsersUseCase,
    private val fetchAnnouncementsUseCase: FetchAnnouncementsUseCase,
    private val fetchMissionsUseCase: FetchMissionsUseCase
) {
    suspend fun execute(userId: String) {
        fetchCurrentUserUseCase.execute(userId)
        fetchBlockedUsersUseCase.execute(userId)
        fetchAnnouncementsUseCase.execute()
        fetchMissionsUseCase.execute()
    }
}