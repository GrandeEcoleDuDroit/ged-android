package com.upsaclay.mission.domain

object MissionUtils {
    const val FOLDER_NAME = "MissionImages"

    fun formatImageFileName(missionId: String): String =
        "${missionId}-mission-image-${System.currentTimeMillis()}"
}