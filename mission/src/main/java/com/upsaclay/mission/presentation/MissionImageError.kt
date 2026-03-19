package com.upsaclay.mission.presentation

import androidx.annotation.StringRes
import com.upsaclay.common.presentation.CommonPresentationUtils.MAX_IMAGE_FILE_SIZE

sealed class MissionImageError(@StringRes val error: Int) {
    data object ImageTooLarge: MissionImageError(error = com.upsaclay.common.R.string.image_too_large_error_message) {
        val LIMIT: Long = MAX_IMAGE_FILE_SIZE
    }
}