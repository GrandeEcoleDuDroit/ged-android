package com.upsaclay.news.domain.post

import com.upsaclay.common.domain.usecase.GenerateIdUseCase

object PostUtils {
    object Image {
        private const val FOLDER_NAME = "PostImages"

        fun generateFileName(postId: String): String =
            "${postId}-post-image-${GenerateIdUseCase().execute()}"

        fun getRelativePath(fileName: String): String = "$FOLDER_NAME/$fileName"

        fun getFileName(url: String?): String? = url?.substringAfterLast("/")
    }
}