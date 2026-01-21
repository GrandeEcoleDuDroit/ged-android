package com.upsaclay.common.data.extensions

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import timber.log.Timber

fun <T> DataStore<Preferences>.getFlowValue(key: Preferences.Key<T>): Flow<T?> = data.map { it[key] }

inline fun <reified T> DataStore<Preferences>.getGsonFlowValue(key: Preferences.Key<String>, gson: Gson = Gson()): Flow<T?> {
    val type = object : TypeToken<T>() {}.type
    return getFlowValue(key).map {
        runCatching { gson.fromJson<T>(it, type) }
            .onFailure { Timber.e("Error getting gson value flow of $key from data store: ${it.message}") }
            .getOrNull()
    }
}

suspend fun <T> DataStore<Preferences>.getValue(key: Preferences.Key<T>): T? = data.map { it[key] }.firstOrNull()

suspend inline fun <reified T> DataStore<Preferences>.getGsonValue(key: Preferences.Key<String>, gson: Gson = Gson()): T? {
    val type = object : TypeToken<T>() {}.type
    return getValue(key)?.let {
        runCatching { gson.fromJson<T>(it, type) }
            .onFailure { Timber.e("Error getting gson value of $key from data store: ${it.message}") }
            .getOrNull()
    }
}

suspend fun <T> DataStore<Preferences>.setValue(key: Preferences.Key<T>, value: T) {
    edit { it[key] = value }
}

suspend fun <T> DataStore<Preferences>.setGsonValue(key: Preferences.Key<String>, value: T, gson: Gson = Gson()) {
    setValue(key, gson.toJson(value))
}

suspend fun <T> DataStore<Preferences>.removeValue(key: Preferences.Key<T>) {
    edit { it.remove(key) }
}

suspend fun <T> DataStore<Preferences>.contains(key: Preferences.Key<T>): Boolean =
    data.map { it.contains(key) }.firstOrNull() ?: false

suspend fun <T> DataStore<Preferences>.clearAll() {
    edit { it.clear() }
}