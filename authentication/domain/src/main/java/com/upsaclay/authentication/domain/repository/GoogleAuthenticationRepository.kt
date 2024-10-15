package com.upsaclay.authentication.domain.repository

interface GoogleAuthenticationRepository {
    suspend fun login(): Result<String>
}