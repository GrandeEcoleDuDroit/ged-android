package com.upsaclay.news.presentation.post

import androidx.annotation.StringRes
import com.upsaclay.news.R
import com.upsaclay.news.presentation.post.PostPresentationUtils.MAX_POST_LINK_LENGTH

sealed class PostLinkError(@StringRes val error: Int) {
    data object ExceedLengthLimit: PostLinkError(error = R.string.post_link_length_error) {
        const val LIMIT: Int = MAX_POST_LINK_LENGTH
    }
}