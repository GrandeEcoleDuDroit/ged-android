package com.upsaclay.mission.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.domain.missionsFixture

class MissionPreviewParameterProvider: PreviewParameterProvider<AllMissionPreviewParameterData> {
    override val values = sequenceOf(AllMissionPreviewParameterData(userFixture, missionsFixture))
}

class MissionDetailsPreviewParameterProvider: PreviewParameterProvider<MissionDetailsPreviewParameterData> {
    override val values = sequenceOf(MissionDetailsPreviewParameterData(missionFixture, userFixture))
}

class AllMissionPreviewParameterProvider: PreviewParameterProvider<AllMissionPreviewParameterData> {
    override val values = sequenceOf(AllMissionPreviewParameterData(userFixture, missionsFixture))
}

class AllUsersMissionDetailsPreviewParameterProvider: PreviewParameterProvider<List<User>> {
    override val values = sequenceOf(usersFixture + usersFixture)
}

data class AllMissionPreviewParameterData(
    val user: User,
    val missions: List<Mission>
)

data class MissionPreviewParameterData(
    val mission: Mission,
    val users: List<User>,
)

data class MissionDetailsPreviewParameterData(
    val mission: Mission,
    val user: User,
)