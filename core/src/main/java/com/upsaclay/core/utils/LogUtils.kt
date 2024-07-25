package com.upsaclay.core.utils

import android.util.Log

fun  Any.debugLog(message: String) {
    Log.d(javaClass.simpleName, message)
}

fun Any.errorLog(message: String, throwable: Throwable? = null) {
    Log.e(javaClass.simpleName, message, throwable)
}

fun Any.infoLog(message: String) {
    Log.i(javaClass.simpleName, message)
}