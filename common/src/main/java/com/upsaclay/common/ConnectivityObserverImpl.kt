package com.upsaclay.common

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import androidx.core.content.getSystemService
import com.upsaclay.common.domain.ConnectivityObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

@SuppressLint("MissingPermission")
class ConnectivityObserverImpl(context: Context): ConnectivityObserver {
    private val connectivityManager = context.getSystemService<ConnectivityManager>()
    override val connected = callbackFlow {
        val callback = object : NetworkCallback() {
            override fun onUnavailable() { trySend(false) }
            override fun onAvailable(network: Network) { trySend(true) }
            override fun onLost(network: Network) { trySend(false) }
        }

        connectivityManager?.registerDefaultNetworkCallback(callback)
        awaitClose { connectivityManager?.unregisterNetworkCallback(callback) }
    }
}