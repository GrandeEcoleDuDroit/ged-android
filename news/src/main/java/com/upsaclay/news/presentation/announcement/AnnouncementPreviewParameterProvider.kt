package com.upsaclay.news.presentation.announcement

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.announcementFixture
import com.upsaclay.news.domain.announcement.announcementsFixture

class AnnouncementPreviewParameterProvider: PreviewParameterProvider<AnnouncementPreviewParameterData> {
    override val values = sequenceOf(AnnouncementPreviewParameterData(userFixture, announcementFixture))
}

class ALlAnnouncementPreviewParameterProvider: PreviewParameterProvider<AllAnnouncementPreviewParameterData> {
    override val values = sequenceOf(AllAnnouncementPreviewParameterData(userFixture, announcementsFixture))
}

data class AnnouncementPreviewParameterData(
    val user: User,
    val announcement: Announcement
)

data class AllAnnouncementPreviewParameterData(
    val user: User,
    val announcements: List<Announcement>
)