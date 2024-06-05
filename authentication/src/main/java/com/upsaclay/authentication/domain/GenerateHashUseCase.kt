package com.upsaclay.authentication.domain

import java.security.MessageDigest

class GenerateHashUseCase(
    private val generateRandomString: GenerateRandomString
) {
    operator fun invoke(): String {
        val secureRandomString = generateRandomString()
        val bytes = secureRandomString.toByteArray()
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}