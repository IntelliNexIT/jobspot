package com.intellinex.jobspot.api.instances

import com.intellinex.jobspot.utils.AuthInterceptor
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.intellinex.jobspot.api.services.AuthService
import com.intellinex.jobspot.api.services.DataService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthClient {
    private var retrofit: Retrofit? = null
    private const val PREFS_NAME = "MY_PREFS"

    fun getAuthClient(context: Context, baseUrl: String,): Retrofit {
        if(retrofit == null){
            val token = getToken(context)
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(token!!))
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }

    suspend fun logout(context: Context, authService: AuthService){
        withContext(Dispatchers.IO) {
            try {
                authService.logout()
            }catch (e: Exception){
                Log.e("AUTH_LOGOUT", e.message.toString())
            } finally {
                clearToken(context)
            }
        }
    }

    // Function to clear the token from SharedPreferences
    private fun clearToken(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("JWT_TOKEN").apply()
    }

    // Function to get the token from SharedPreferences
    private fun getToken(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("JWT_TOKEN", null)
    }
}