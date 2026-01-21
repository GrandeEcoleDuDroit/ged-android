package com.upsaclay.common.data

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: TokenProvider): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        runBlocking { tokenProvider.getAuthIdToken() }
            ?.let { requestBuilder.addHeader("Authorization", "Bearer $it") }

        return chain.proceed(requestBuilder.build())
    }
}

interface TokenProvider {
    suspend fun getAuthIdToken(): String?
}