package com.upsaclay.gedoise.domain.repository

interface SendMailRepository {
    fun sendMail(message : String)
}