package com.intellinex.jobspot.api.instances

import com.intellinex.jobspot.R
import com.intellinex.jobspot.api.services.ApiService
import com.intellinex.jobspot.ui.fragment.CareerFragment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
//    private const val BASE_URL = "http://192.168.1.5:8001/api/"
//
//    val instance: ApiService by lazy {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        retrofit.create(ApiService::class.java)
//    }
//
    private var apiService: ApiService? = null
    fun getApi(context: CareerFragment): ApiService {
        if(apiService == null){
            var apiUrl = context.getString(R.string.api_url)
            apiService = Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
        return apiService!!
    }
}