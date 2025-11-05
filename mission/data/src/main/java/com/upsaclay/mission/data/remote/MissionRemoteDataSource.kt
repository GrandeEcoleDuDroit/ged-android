package com.upsaclay.mission.data.remote

import com.google.gson.Gson
import com.upsaclay.common.data.exceptions.mapServerResponseException
import com.upsaclay.mission.data.toRemote
import com.upsaclay.mission.domain.entity.Mission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class MissionRemoteDataSource(private val missionApi: MissionApi) {
    suspend fun createMission(mission: Mission, imageFile: File?) {
        val missionPart = Gson().toJson(mission.toRemote(imageFile?.name)).toRequestBody("application/json".toMediaType())
        val imagePart = imageFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("image", it.name, requestFile)
        }

        withContext(Dispatchers.IO) {
            mapServerResponseException(
                block = { missionApi.createMission(imagePart, missionPart) }
            )
        }
    }

    suspend fun deleteMission(missionId: Int, imageUrl: String?) {
        withContext(Dispatchers.IO) {
            mapServerResponseException(
                block = { missionApi.deleteMission(missionId, imageUrl) },
                message = "Failed to delete mission"
            )
        }
    }
}