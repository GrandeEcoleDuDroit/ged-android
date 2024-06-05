package com.upsaclay.authentication.domain

import java.security.SecureRandom

class GenerateRandomString {
    private val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZaàäbc" +
            "deéèëêfghijklmnopqrstuùûüvwxyz0123456789!" +
            "@#\\\$£€%^{}[]&*()-_ =~+<>?/.,"
    private val secureRandom = SecureRandom()
    private val randomString = StringBuilder()
    operator fun invoke(): String {
        for (i in 0..20) {
            val randomIndex = secureRandom.nextInt(chars.length)
            randomString.append(chars[randomIndex])
        }
        return randomString.toString()
    }
}