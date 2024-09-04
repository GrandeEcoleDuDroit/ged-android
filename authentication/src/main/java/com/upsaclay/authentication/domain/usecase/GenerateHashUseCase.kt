package com.upsaclay.authentication.domain.usecase

import java.security.MessageDigest

class GenerateHashUseCase(
    private val generateRandomStringUseCase: GenerateRandomStringUseCase
) {
    operator fun invoke(): String {
        val secureRandomString = generateRandomStringUseCase()
        val bytes = secureRandomString.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}