package com.upsaclay.authentication.domain

import java.security.SecureRandom

class GenerateRandomString {
    private val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZaàäbc" +
            "deéèëêfghijklmnoôöpqrstuùûüµvwxyz0123456789!" +
            "@#\\\$£€%^¨{}[]&*()-_ =~+<>?/|.,;:¤"
    private val secureRandom = SecureRandom()
    operator fun invoke(): String {
        val randomString = StringBuilder()
        repeat(20) {
            val randomIndex = secureRandom.nextInt(chars.length)
            randomString.append(chars[randomIndex])
        }
        return randomString.toString()
    }
}