package com.intellinex.jobspot.utils

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.os.Build
import androidx.lifecycle.LiveData

class NetworkConnection(private val context: Context): LiveData<Boolean>() {
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var networkConnectionCallback: ConnectivityManager.NetworkCallback? = null




    @SuppressLint("ObsoleteSdkInt")
    override fun onActive() {
        super.onActive()
        updateNetworkConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                if (networkConnectionCallback == null) {
                    networkConnectionCallback = object : ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network) {
                            postValue(true)
                        }

                        override fun onLost(network: Network) {
                            postValue(false)
                        }
                    }
                }
                connectivityManager.registerDefaultNetworkCallback(networkConnectionCallback!!)
            }
            else -> {
                context.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        networkConnectionCallback?.let {
            try {
                connectivityManager.unregisterNetworkCallback(it)
            } catch (e: IllegalArgumentException) {
                // Handle the case where the callback was not registered
                e.printStackTrace()
            } finally {
                networkConnectionCallback = null // Clear reference
            }
        }
    }

    private fun updateNetworkConnection() {
        val networkConnection: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(networkConnection?.isConnected == true)
    }


    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateNetworkConnection()
        }
    }

}