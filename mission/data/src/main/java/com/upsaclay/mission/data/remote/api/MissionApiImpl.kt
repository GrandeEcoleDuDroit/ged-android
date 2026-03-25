package com.upsaclay.mission.data.remote.api

import com.google.gson.Gson
import com.upsaclay.common.data.UserField.Oracle.USER_ID
import com.upsaclay.common.data.UserField.Oracle.USER_SCHOOL_LEVEL
import com.upsaclay.common.data.remote.model.OracleUser
import com.upsaclay.common.data.remote.model.ServerResponse
import com.upsaclay.common.data.utils.sendDataServerRequest
import com.upsaclay.common.data.utils.sendServerRequest
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
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File

internal class MissionApiImpl(private val missionServerApi: MissionServerApi): MissionApi {
    private val gson = Gson()

    override suspend fun getMissions(): List<InboundRemoteMission>? {
        return sendDataServerRequest {
            missionServerApi.getMissions()
        }
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

        sendServerRequest {
            missionServerApi.createMission(imagePart, missionPart)
        }
    }

    override suspend fun updateMission(userId: String, remoteMission: OutboundRemoteMission, imageFile: File?) {
        var imagePart: MultipartBody.Part? = null

        val userIdPart = userId.toRequestBody("text/plain".toMediaType())

        val missionPart = gson
            .toJson(remoteMission)
            .toRequestBody("application/json".toMediaType())

        if (imageFile != null && remoteMission.missionImageFileName != null) {
            imagePart = MultipartBody.Part.createFormData(
                "image",
                remoteMission.missionImageFileName,
                imageFile.asRequestBody("image/*".toMediaType()))
        }

        sendServerRequest {
            missionServerApi.updateMission(imagePart, userIdPart, missionPart)
        }
    }

    override suspend fun deleteMission(missionId: String) {
        sendServerRequest {
            missionServerApi.deleteMission(missionId)
        }
    }

    override suspend fun addParticipant(missionId: String, oracleUser: OracleUser) {
        sendServerRequest {
            missionServerApi.addParticipant(
                missionId,
                oracleUser.userId,
                oracleUser.userSchoolLevel.toString()
            )
        }
    }

    override suspend fun removeParticipant(missionId: String, userId: String) {
        sendServerRequest {
            missionServerApi.removeParticipant(missionId, userId)
        }
    }

    override suspend fun reportMission(remoteMissionReport: RemoteMissionReport) {
        sendServerRequest {
            missionServerApi.reportMission(remoteMissionReport)
        }
    }
}

internal interface MissionServerApi {
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
        @Part(USER_ID) userId: RequestBody,
        @Part("mission") mission: RequestBody
    ): Response<ServerResponse>

    @DELETE("missions/{missionId}")
    suspend fun deleteMission(@Path("missionId") missionId: String): Response<ServerResponse>

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