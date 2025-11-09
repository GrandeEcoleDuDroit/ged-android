package com.upsaclay.mission.domain

object MissionUtils {
    fun imageFileName(missionId: String): String = "${missionId}-mission-image-${System.currentTimeMillis()}"
}