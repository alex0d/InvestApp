package ru.alex0d.investapp.utils.connectivity

import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkConnectivityObserver(
    private val context: Context
) : ConnectivityObserver {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: android.net.Network) {
                    trySend(ConnectivityObserver.Status.AVAILABLE)
                }

                override fun onLost(network: android.net.Network) {
                    trySend(ConnectivityObserver.Status.LOST)
                }

                override fun onLosing(network: android.net.Network, maxMsToLive: Int) {
                    trySend(ConnectivityObserver.Status.LOSING)
                }

                override fun onUnavailable() {
                    trySend(ConnectivityObserver.Status.UNAVAILABLE)
                }
            }

            connectivityManager.registerDefaultNetworkCallback(callback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }.distinctUntilChanged()
    }
}