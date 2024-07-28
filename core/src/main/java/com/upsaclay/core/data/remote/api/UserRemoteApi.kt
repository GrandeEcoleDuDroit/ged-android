package com.upsaclay.core.data.remote.api

import com.upsaclay.core.data.model.ServerResponse
import com.upsaclay.core.data.model.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface UserRemoteApi {
    @POST("/users/create")
    suspend fun createUser(@Body user: UserDTO): Response<ServerResponse.IntServerResponse>
}