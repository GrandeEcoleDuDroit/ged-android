package com.upsaclay.mission.data.remote.api

import com.google.gson.Gson
import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.common.data.remote.model.ServerResponse
import com.upsaclay.mission.data.remote.InboundRemoteMission
import com.upsaclay.mission.data.toMission
import com.upsaclay.mission.data.toRemote
import com.upsaclay.mission.domain.entity.Mission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
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
        )?.map(InboundRemoteMission::toMission) ?: emptyList()
    }

    override suspend fun createMission(mission: Mission, imageFile: File?) {
        val missionPart = gson
            .toJson(mission.toRemote(imageFile?.name))
            .toRequestBody("application/json".toMediaType())

        val imagePart = imageFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("image", it.name, requestFile)
        }

        withContext(Dispatchers.IO) {
            mapServerResponseException(
                message = "Failed to create mission",
                block = { serverMissionApi.createMission(imagePart, missionPart) }
            )
        }
    }

    override suspend fun updateMission(mission: Mission, imageFile: File?) {
        val missionPart = gson
            .toJson(mission.toRemote(imageFile?.name))
            .toRequestBody("application/json".toMediaType())

        val imagePart = imageFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("image", it.name, requestFile)
        }

        withContext(Dispatchers.IO) {
            mapServerResponseException(
                message = "Failed to update mission",
                block = { serverMissionApi.updateMission(mission.id.toString(), imagePart, missionPart) }
            )
        }
    }

    override suspend fun deleteMission(missionId: Long, imageUrl: String?) {
        withContext(Dispatchers.IO) {
            mapServerResponseException(
                message = "Failed to delete mission",
                block = { serverMissionApi.deleteMission(missionId, imageUrl) },
            )
        }
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

    @Multipart
    @POST("missions/delete")
    suspend fun deleteMission(
        @Part missionId: Long,
        @Part imageUrl: String?
    ): Response<ServerResponse>
}