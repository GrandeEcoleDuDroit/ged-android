package com.upsaclay.mission.domain

object MissionUtils {
    object Image {
        private const val FOLDER_NAME = "MissionImages"

        fun generateFileName(missionId: String): String =
            "${missionId}-mission-image-${System.currentTimeMillis()}"

        fun makeRelativePath(fileName: String): String = "$FOLDER_NAME/$fileName"

        fun getFileName(url: String?): String? = url?.substringAfterLast("/")

        fun getPath(url: String?): String? = getFileName(url)?.let(::makeRelativePath)
    }
}