package com.upsaclay.authentication.data.remote

import com.upsaclay.core.data.model.ServerResponse
import retrofit2.Response

class AuthenticationRemoteDataSource(
    private val authenticationParisSaclayApi: AuthenticationParisSaclayApi,
) {
    suspend fun loginWithParisSaclay(
        email: String,
        password: String,
        hash: String
    ): Response<ServerResponse.EmptyServerResponse> {
        return authenticationParisSaclayApi.login(
            username = email,
            password = password,
            hash = hash
        )
    }
}