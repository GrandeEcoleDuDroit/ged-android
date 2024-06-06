package com.upsaclay.core.domain

import android.content.Context
import android.content.SharedPreferences
import com.upsaclay.core.data.SharedPreferenceFiles
import com.upsaclay.core.data.SharedPreferencesKeys

class SharedPreferenceUseCase(private val context: Context) {
    private lateinit var sharedPreferences: SharedPreferences

    fun storeBoolean(
        file: SharedPreferenceFiles,
        key: SharedPreferencesKeys,
        value: Boolean
    ) {
        sharedPreferences = context.getSharedPreferences(file.filename, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean(key.keyname, value)
            .apply()
    }

    fun getBoolean(
        file: SharedPreferenceFiles,
        key: SharedPreferencesKeys,
        defaultValue: Boolean
    ): Boolean {
        sharedPreferences = context.getSharedPreferences(file.filename, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key.keyname, defaultValue)
    }
}