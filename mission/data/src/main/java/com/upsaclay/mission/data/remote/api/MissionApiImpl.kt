package com.upsaclay.mission.data.remote.api

import com.google.gson.Gson
import com.upsaclay.common.data.UserField.Server.USER_ID
import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.common.data.remote.model.ServerResponse
import com.upsaclay.mission.data.MissionField.Remote.MISSION_ID
import com.upsaclay.mission.data.MissionField.Remote.MISSION_IMAGE_FILE_NAME
import com.upsaclay.mission.data.mapper.toMission
import com.upsaclay.mission.data.mapper.toRemote
import com.upsaclay.mission.data.remote.models.InboundRemoteMission
import com.upsaclay.mission.data.remote.models.RemoteAddMissionParticipant
import com.upsaclay.mission.data.remote.models.RemoteMissionReport
import com.upsaclay.mission.domain.entity.AddMissionParticipant
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionReport
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File

internal class MissionApiImpl(
    private val serverMissionApi: ServerMissionApi
): MissionApi {
    private val gson = Gson()

    override suspend fun getMissions(): List<Mission> {
        return mapServerResponseException(
            message = "Failed to get missions",
            block = { serverMissionApi.getMissions() }
        )?.map { it.toMission() } ?: emptyList()
    }

    override suspend fun createMission(mission: Mission, imageFile: File?) {
        val missionPart = gson
            .toJson(mission.toRemote())
            .toRequestBody("application/json".toMediaType())

        val imagePart = imageFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("image", it.name, requestFile)
        }

        mapServerResponseException(
            message = "Failed to create mission",
            block = { serverMissionApi.createMission(imagePart, missionPart) }
        )
    }

    override suspend fun updateMission(mission: Mission, imageFile: File?) {
        val missionPart = gson
            .toJson(mission.toRemote())
            .toRequestBody("application/json".toMediaType())

        val imagePart = imageFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("image", it.name, requestFile)
        }

        mapServerResponseException(
            message = "Failed to update mission",
            block = { serverMissionApi.updateMission(mission.id, imagePart, missionPart) }
        )
    }

    override suspend fun deleteMission(missionId: String, imageFileName: String?) {
        mapServerResponseException(
            message = "Failed to delete mission",
            block = { serverMissionApi.deleteMission(missionId, imageFileName) },
        )
    }

    override suspend fun reportMission(report: MissionReport) {
        mapServerResponseException(
            message = "Failed to report mission",
            block = { serverMissionApi.reportMission(report.toRemote()) }
        )
    }

    override suspend fun addParticipant(addMissionParticipant: AddMissionParticipant) {
        mapServerResponseException(
            message = "Failed to add participant to mission",
            block = { serverMissionApi.addParticipant(addMissionParticipant.toRemote()) }
        )
    }

    override suspend fun removeParticipant(missionId: String, userId: String) {
        mapServerResponseException(
            message = "Failed to remove participant from mission",
            block = { serverMissionApi.removeParticipant(missionId, userId) }
        )
    }
}

internal interface ServerMissionApi {
    @GET("missions")
    suspend fun getMissions(): Response<List<InboundRemoteMission>>

    @Multipart
    @POST("missions/create")
    suspend fun createMission(
        @Part image: MultipartBody.Part?,
        @Part("mission") remoteMission: RequestBody
    ): Response<ServerResponse>

    @Multipart
    @PUT("missions/{missionId}")
    suspend fun updateMission(
        @Path("missionId") missionId: String,
        @Part image: MultipartBody.Part?,
        @Part("mission") remoteMission: RequestBody
    ): Response<ServerResponse>

    @FormUrlEncoded
    @POST("missions/delete")
    suspend fun deleteMission(
        @Field(MISSION_ID) missionId: String,
        @Field(MISSION_IMAGE_FILE_NAME) missionImageFileName: String?
    ): Response<ServerResponse>

    @POST("missions/report")
    suspend fun reportMission(@Body remoteMissionReport: RemoteMissionReport): Response<ServerResponse>

    @POST("missions/add-participant")
    suspend fun addParticipant(
        @Body remoteAddMissionParticipant: RemoteAddMissionParticipant
    ): Response<ServerResponse>

    @FormUrlEncoded
    @POST("missions/remove-participant")
    suspend fun removeParticipant(
        @Field(MISSION_ID) missionId: String,
        @Field(USER_ID) userId: String
    ): Response<ServerResponse>
}