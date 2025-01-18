package com.intellinex.jobspot.api.instances

import android.content.Context
import com.intellinex.jobspot.R
import com.intellinex.jobspot.api.services.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceProvider {

    private var apiService: ApiService? = null
    fun getApi(context: Context): ApiService {
        if(ApiServiceProvider.apiService == null){
            val apiUrl = context.getString(R.string.api_url)
            ApiServiceProvider.apiService = Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
        return ApiServiceProvider.apiService!!
    }

}