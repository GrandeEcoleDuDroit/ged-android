package com.upsaclay.news.presentation.post

import androidx.annotation.StringRes
import com.upsaclay.common.presentation.CommonPresentationUtils.MAX_IMAGE_FILE_SIZE
import com.upsaclay.news.R
import com.upsaclay.news.presentation.post.PostPresentationUtils.MAX_IMAGE_COUNT

sealed class PostImageError(@StringRes val error: Int) {
    data object TooManyImages: PostImageError(error = R.string.post_max_image_count_error) {
        const val LIMIT: Int = MAX_IMAGE_COUNT
    }

    data object ImageTooLarge: PostImageError(error = com.upsaclay.common.R.string.image_too_large_error_message) {
        val LIMIT: Long = MAX_IMAGE_FILE_SIZE
    }
}