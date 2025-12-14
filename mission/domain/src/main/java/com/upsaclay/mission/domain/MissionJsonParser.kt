package com.upsaclay.mission.domain

import com.google.gson.GsonBuilder
import com.upsaclay.common.domain.adapter.LocalDateAdapter
import com.upsaclay.common.domain.adapter.LocalDateTimeAdapter
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import java.time.LocalDate
import java.time.LocalDateTime

object MissionJsonParser {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter)
        .registerTypeAdapter(MissionState::class.java, MissionStateAdapter)
        .create()

    fun toMission(missionJson: String): Mission = gson.fromJson(missionJson, Mission::class.java)

    fun toJson(mission: Mission): String = gson.toJson(mission)
}