package com.upsaclay.mission.data.remote.api

import com.google.gson.Gson
import com.upsaclay.common.data.UserField.Server.USER_ID
import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.common.data.remote.model.ServerResponse
import com.upsaclay.mission.data.MissionField.Remote.MISSION_ID
import com.upsaclay.mission.data.mapper.toMission
import com.upsaclay.mission.data.mapper.toRemote
import com.upsaclay.mission.data.remote.models.InboundRemoteMission
import com.upsaclay.mission.data.remote.models.RemoteAddMissionParticipant
import com.upsaclay.mission.data.remote.models.RemoteMissionReport
import com.upsaclay.mission.domain.MissionUtils
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
import retrofit2.http.Part
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
        var imagePart: MultipartBody.Part? = null
        var imagePathPart: RequestBody? = null

        val remoteMission = mission.toRemote()
        val missionPart = gson
            .toJson(remoteMission)
            .toRequestBody("application/json".toMediaType())

        if (imageFile != null && remoteMission.missionImageFileName != null) {
            val requestFile = imageFile.asRequestBody("image/*".toMediaType())
            imagePart = MultipartBody.Part.createFormData("image", remoteMission.missionImageFileName, requestFile)

            imagePathPart = remoteMission.missionImageFileName
                .let { MissionUtils.Image.makeRelativePath(it) }
                .toRequestBody("text/plain".toMediaType())
        }

        mapServerResponseException(
            message = "Failed to create mission",
            block = {
                serverMissionApi.createMission(
                    image = imagePart,
                    imagePath = imagePathPart,
                    mission = missionPart
                )
            }
        )
    }

    override suspend fun updateMission(mission: Mission, imageFile: File?) {
        var imagePart: MultipartBody.Part? = null
        var imagePathPart: RequestBody? = null

        val remoteMission = mission.toRemote()
        val missionPart = gson
            .toJson(mission.toRemote())
            .toRequestBody("application/json".toMediaType())

        if (imageFile != null && remoteMission.missionImageFileName != null) {
            val requestFile = imageFile.asRequestBody("image/*".toMediaType())
            imagePart = MultipartBody.Part.createFormData("image", remoteMission.missionImageFileName, requestFile)

            imagePathPart = remoteMission.missionImageFileName
                .let { MissionUtils.Image.makeRelativePath(it) }
                .toRequestBody("text/plain".toMediaType())
        }

        mapServerResponseException(
            message = "Failed to update mission",
            block = {
                serverMissionApi.updateMission(
                    image = imagePart,
                    imagePath = imagePathPart,
                    mission = missionPart
                )
            }
        )
    }

    override suspend fun deleteMission(missionId: String, imageFileName: String?) {
        val imagePath = imageFileName?.let { MissionUtils.Image.makeRelativePath(it) }

        mapServerResponseException(
            message = "Failed to delete mission",
            block = { serverMissionApi.deleteMission(missionId, imagePath) },
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
        @Part("imagePath") imagePath: RequestBody?,
        @Part("mission") mission: RequestBody
    ): Response<ServerResponse>

    @Multipart
    @POST("missions/update")
    suspend fun updateMission(
        @Part image: MultipartBody.Part?,
        @Part("imagePath") imagePath: RequestBody?,
        @Part("mission") mission: RequestBody
    ): Response<ServerResponse>

    @FormUrlEncoded
    @POST("missions/delete")
    suspend fun deleteMission(
        @Field(MISSION_ID) missionId: String,
        @Field("imagePath") imagePath: String?
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