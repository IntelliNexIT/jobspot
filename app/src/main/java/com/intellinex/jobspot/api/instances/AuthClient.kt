package com.intellinex.jobspot.api.instances

import com.intellinex.jobspot.utils.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthClient {
    private var retrofit: Retrofit? = null

    fun getAuthClient(baseUrl: String, token: String): Retrofit {
        if(retrofit == null){
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(token))
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}