package com.upsaclay.mission.data.remote

import com.upsaclay.common.data.remote.model.ServerResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MissionApi {
    @Multipart
    @POST("missions/create")
    suspend fun createMission(
        @Part image: MultipartBody.Part?,
        @Part("mission") remoteMission: RequestBody
    ): Response<ServerResponse>

    @Multipart
    @POST("missions/delete")
    suspend fun deleteMission(
        @Part missionId: Int,
        @Part imageUrl: String?
    ): Response<ServerResponse>
}