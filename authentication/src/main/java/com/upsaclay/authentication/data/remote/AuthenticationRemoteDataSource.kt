package com.upsaclay.authentication.data.remote

import com.upsaclay.authentication.domain.GenerateHashUseCase
import com.upsaclay.core.data.model.ServerResponse
import com.upsaclay.core.data.model.UserDTO
import com.upsaclay.core.data.remote.UserApi
import com.upsaclay.core.utils.errorLog
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class AuthenticationRemoteDataSource(
    private val userApi: UserApi,
    private val authenticationParisSaclayApi: AuthenticationParisSaclayApi,
    private val generateHashUseCase: GenerateHashUseCase
) {
    suspend fun login(email: String, password: String): Response<ServerResponse.EmptyServerResponse> {
        val hash = generateHashUseCase()
        return authenticationParisSaclayApi.login(
            username = email,
            password = password,
            hash = hash
        )
    }

    suspend fun createUser(user: UserDTO): Response<ServerResponse.IntServerResponse> {
        return try {
            userApi.createUser(user)
        }
        catch (e: Exception) {
            errorLog("Error to create user: ${e.message.toString()}")
            Response.error(500, e.message.toString().toResponseBody())
        }
    }
}