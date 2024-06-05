package com.upsaclay.authentication.data

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

class AuthenticationRepositoryImpl : AuthenticationRepository {
    private val okHttpClient = OkHttpClient()

    companion object {
        private const val AUTH_URL = "https://adonis-api.universite-paris-saclay.fr/v1/auth/signin"
    }

    override suspend fun loginWithParisSaclay(
        email: String, password: String, hash: String
    ): Result<AuthenticationState> {
        val requestBody = FormBody.Builder()
            .add("username", email)
            .add("password", password)
            .add("state", hash)
            .build()

        val request = Request.Builder()
            .url(AUTH_URL)
            .post(requestBody)
            .build()

        val response: Response = okHttpClient.newCall(request).execute()
        return if (response.isSuccessful)
            Result.success(AuthenticationState.AUTHENTICATED)
        else Result.failure(IOException("Error authentication request : $response"))
    }
}