package com.upsaclay.news.presentation.post

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.post.Post
import com.upsaclay.news.domain.post.postFixture
import com.upsaclay.news.domain.post.postsFixture

class PostPreviewParameterProvider: PreviewParameterProvider<PostPreviewParameterData> {
    override val values = sequenceOf(PostPreviewParameterData(userFixture, postFixture))
}

class AllPostPreviewParameterProvider: PreviewParameterProvider<AllPostPreviewParameterData> {
    override val values = sequenceOf(AllPostPreviewParameterData(userFixture, postsFixture))
}

data class PostPreviewParameterData(
    val user: User,
    val post: Post
)

data class AllPostPreviewParameterData(
    val user: User,
    val posts: List<Post>
)