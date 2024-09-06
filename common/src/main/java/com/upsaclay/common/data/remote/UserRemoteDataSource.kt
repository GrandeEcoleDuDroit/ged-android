package com.upsaclay.common.data.remote

import com.upsaclay.common.data.model.UserDTO
import com.upsaclay.common.data.remote.api.UserRemoteApi
import com.upsaclay.common.domain.model.ServerResponse.EmptyResponse
import com.upsaclay.common.domain.model.ServerResponse.IntResponse
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.utils.e
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

internal class UserRemoteDataSource(private val userRemoteApi: UserRemoteApi) {
    suspend fun getUser(userId: Int): Result<User?> = withContext(Dispatchers.IO) {
        try {
            val response = userRemoteApi.getUser(userId)
            if (response.isSuccessful) {
                val userDTO = response.body()
                userDTO?.let {
                    Result.success(it.toDomain())
                } ?: run {
                    Result.success(null)
                }
            } else {
                Result.failure(Exception("Error retrieving user : ${response.message()}"))
            }
        } catch (e: Exception) {
            e("Error retrieving user: ${e.message.toString()}")
            Result.failure(Exception("Error retrieving user : ${e.message}"))
        }
    }

    suspend fun createUser(user: User): Response<IntResponse> = withContext(Dispatchers.IO) {
        try {
            val userDTO = UserDTO.fromDomain(user)
            userRemoteApi.createUser(userDTO)
        } catch (e: Exception) {
            e("Error creating user: ${e.message.toString()}")
            Response.error(500, e.message.toString().toResponseBody())
        }
    }

    suspend fun updateProfilePictureUrl(userId: Int, newProfilePictureUrl: String): Response<EmptyResponse> {
        return withContext(Dispatchers.IO) {
            try {
                userRemoteApi.updateProfilePictureUrl(userId, newProfilePictureUrl)
            } catch (e: Exception) {
                e("Error updating user profile picture url: ${e.message.toString()}", e)
                Response.error(500, e.message.toString().toResponseBody())
            }
        }
    }

    suspend fun deleteProfilePictureUrl(userId: Int): Response<EmptyResponse> = withContext(Dispatchers.IO) {
        try {
            userRemoteApi.deleteProfilePictureUrl(userId)
        } catch (e: Exception) {
            e("Error deleting user profile picture url: ${e.message.toString()}", e)
            Response.error(500, e.message.toString().toResponseBody())
        }
    }
}