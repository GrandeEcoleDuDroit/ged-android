package com.upsaclay.common.data.remote

import com.upsaclay.common.data.model.ServerResponse
import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.data.remote.api.UserRemoteApi
import com.upsaclay.common.utils.e
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

internal class UserRemoteDataSource(private val userRemoteApi: UserRemoteApi) {
    suspend fun createUser(user: UserDTO): Response<ServerResponse.IntResponse> = withContext(Dispatchers.IO) {
        try {
            userRemoteApi.createUser(user)
        } catch (e: Exception) {
            e("Error to create user: ${e.message.toString()}")
            Response.error(500, e.message.toString().toResponseBody())
        }
    }

    suspend fun updateProfilePictureUrl(userId: Int, newProfilePictureUrl: String): Response<ServerResponse.EmptyResponse> {
        return withContext(Dispatchers.IO) {
            try {
                userRemoteApi.updateProfilePictureUrl(userId, newProfilePictureUrl)
            } catch (e: Exception) {
                e("Error to update user profile picture url: ${e.message.toString()}", e)
                Response.error(500, e.message.toString().toResponseBody())
            }
        }
    }
}