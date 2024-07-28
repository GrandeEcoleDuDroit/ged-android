package com.upsaclay.core.data.remote

import com.upsaclay.core.data.model.ServerResponse
import com.upsaclay.core.data.model.UserDTO
import com.upsaclay.core.data.remote.api.UserRemoteApi
import com.upsaclay.core.utils.errorLog
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

internal class UserRemoteDataSource(private val userRemoteApi: UserRemoteApi) {
    suspend fun createUser(user: UserDTO): Response<ServerResponse.IntServerResponse> {
        return try {
            userRemoteApi.createUser(user)
        } catch (e: Exception) {
            errorLog("Error to create user: ${e.message.toString()}")
            Response.error(500, e.message.toString().toResponseBody())
        }
    }
}