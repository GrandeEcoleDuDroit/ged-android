package com.upsaclay.authentication.data.remote

import com.upsaclay.common.domain.model.ServerResponse
import retrofit2.Response

internal class AuthenticationRemoteDataSource(
    private val authenticationParisSaclayApi: AuthenticationParisSaclayApi,
) {
    suspend fun loginWithParisSaclay(
        email: String,
        password: String,
        hash: String
    ): Response<ServerResponse.EmptyResponse> {
        return authenticationParisSaclayApi.login(email, password, hash)
    }
}