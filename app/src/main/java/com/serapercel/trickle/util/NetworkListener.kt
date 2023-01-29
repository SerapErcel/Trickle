package com.serapercel.trickle.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkListener : ConnectivityManager.NetworkCallback() {

    private val isNetworkAvailable = MutableStateFlow(false)

    @RequiresApi(Build.VERSION_CODES.N)
    fun checkNetworkAvailabiliy(context: Context): MutableStateFlow<Boolean> {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)

        val network = connectivityManager.activeNetwork
        if (network == null) {
            isNetworkAvailable.value = false
            return isNetworkAvailable
        }

        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        if (networkCapabilities == null) {
            isNetworkAvailable.value = false
            return isNetworkAvailable
        }

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            ) -> {
                isNetworkAvailable.value = true
                isNetworkAvailable
            }
            else -> {
                isNetworkAvailable.value = false
                isNetworkAvailable
            }
        }
    }

    override fun onAvailable(network: Network) {
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvailable
    }
}