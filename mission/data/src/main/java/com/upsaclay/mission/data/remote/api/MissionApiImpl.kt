package com.upsaclay.mission.data.remote.api

import com.google.gson.Gson
import com.upsaclay.common.data.UserField.Oracle.USER_ID
import com.upsaclay.common.data.UserField.Oracle.USER_SCHOOL_LEVEL
import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.common.data.remote.model.OracleUser
import com.upsaclay.common.data.remote.model.ServerResponse
import com.upsaclay.mission.data.MissionField.Remote.MISSION_ID
import com.upsaclay.mission.data.remote.models.InboundRemoteMission
import com.upsaclay.mission.data.remote.models.OutboundRemoteMission
import com.upsaclay.mission.data.remote.models.RemoteMissionReport
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

internal class MissionApiImpl(private val serverMissionApi: ServerMissionApi): MissionApi {
    private val gson = Gson()

    override suspend fun getMissions(): List<InboundRemoteMission>? {
        return mapServerResponseException(
            message = "Failed to get missions",
            block = { serverMissionApi.getMissions() }
        )
    }

    override suspend fun createMission(remoteMission: OutboundRemoteMission, imageFile: File?) {
        var imagePart: MultipartBody.Part? = null

        val missionPart = gson
            .toJson(remoteMission)
            .toRequestBody("application/json".toMediaType())

        if (imageFile != null && remoteMission.missionImageFileName != null) {
            imagePart = MultipartBody.Part.createFormData(
                "image",
                remoteMission.missionImageFileName,
                imageFile.asRequestBody("image/*".toMediaType())
            )
        }

        mapServerResponseException(
            message = "Failed to create mission",
            block = { serverMissionApi.createMission(imagePart, missionPart) }
        )
    }

    override suspend fun updateMission(remoteMission: OutboundRemoteMission, imageFile: File?) {
        var imagePart: MultipartBody.Part? = null

        val missionPart = gson
            .toJson(remoteMission)
            .toRequestBody("application/json".toMediaType())

        if (imageFile != null && remoteMission.missionImageFileName != null) {
            imagePart = MultipartBody.Part.createFormData(
                "image",
                remoteMission.missionImageFileName,
                imageFile.asRequestBody("image/*".toMediaType()))
        }

        mapServerResponseException(
            message = "Failed to update mission",
            block = { serverMissionApi.updateMission(imagePart, missionPart) }
        )
    }

    override suspend fun deleteMission(remoteMission: OutboundRemoteMission) {
        mapServerResponseException(
            message = "Failed to delete mission",
            block = { serverMissionApi.deleteMission(remoteMission) },
        )
    }

    override suspend fun addParticipant(missionId: String, oracleUser: OracleUser) {
        mapServerResponseException(
            message = "Failed to add participant to mission",
            block = { serverMissionApi.addParticipant(missionId, oracleUser.userId, oracleUser.userSchoolLevel.toString()) }
        )
    }

    override suspend fun removeParticipant(missionId: String, userId: String) {
        mapServerResponseException(
            message = "Failed to remove participant from mission",
            block = { serverMissionApi.removeParticipant(missionId, userId) }
        )
    }

    override suspend fun reportMission(remoteMissionReport: RemoteMissionReport) {
        mapServerResponseException(
            message = "Failed to report mission",
            block = { serverMissionApi.reportMission(remoteMissionReport) }
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
        @Part("mission") mission: RequestBody
    ): Response<ServerResponse>

    @Multipart
    @POST("missions/update")
    suspend fun updateMission(
        @Part image: MultipartBody.Part?,
        @Part("mission") mission: RequestBody
    ): Response<ServerResponse>

    @POST("missions/delete")
    suspend fun deleteMission(@Body remoteMission: OutboundRemoteMission): Response<ServerResponse>

    @FormUrlEncoded
    @POST("missions/add-participant")
    suspend fun addParticipant(
        @Field(MISSION_ID) missionId: String,
        @Field(USER_ID) userId: String,
        @Field(USER_SCHOOL_LEVEL) userSchoolLevel: String
    ): Response<ServerResponse>

    @FormUrlEncoded
    @POST("missions/remove-participant")
    suspend fun removeParticipant(
        @Field(MISSION_ID) missionId: String,
        @Field(USER_ID) userId: String
    ): Response<ServerResponse>

    @POST("missions/report")
    suspend fun reportMission(@Body remoteMissionReport: RemoteMissionReport): Response<ServerResponse>
}