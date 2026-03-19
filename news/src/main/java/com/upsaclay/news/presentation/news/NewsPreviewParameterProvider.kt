package com.upsaclay.news.presentation.news

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.announcementsFixture
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.postsFixture

class NewsPreviewParameterProvider: PreviewParameterProvider<NewsPreviewParameterData> {
    override val values = sequenceOf(NewsPreviewParameterData(userFixture, announcementsFixture, postsFixture))
}

data class NewsPreviewParameterData(
    val user: User,
    val announcements: List<Announcement>,
    val posts: List<Post>
)