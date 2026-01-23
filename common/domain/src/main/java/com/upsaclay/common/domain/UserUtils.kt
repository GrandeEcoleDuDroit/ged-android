package com.upsaclay.common.domain

object UserUtils {
    object ProfilePicture {
        private const val FOLDER_NAME = "UserProfilePictures"

        fun generateFileName(userId: String) = "${userId}-profile-picture-${System.currentTimeMillis()}"

        fun makeRelativePath(fileName: String): String = "$FOLDER_NAME/$fileName"

        fun getFileName(url: String?): String? = url?.substringAfterLast("/")

        fun getPath(url: String?): String? = getFileName(url)?.let(::makeRelativePath)
    }
}