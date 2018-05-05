package br.com.rodrigohsb.challenge.webservice

import android.net.ConnectivityManager

/**
 * @rodrigohsb
 */
class NetworkService(var connectivityManager: ConnectivityManager) {

    val isConnected: Boolean
        get() {
            val activeNetwork = connectivityManager.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
}