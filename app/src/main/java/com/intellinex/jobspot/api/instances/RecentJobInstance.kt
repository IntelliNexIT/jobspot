package com.intellinex.jobspot.api.instances

import com.intellinex.jobspot.R
import com.intellinex.jobspot.api.services.RecentJobService
import com.intellinex.jobspot.ui.fragment.HomeFragment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RecentJobInstance {

//    private const val BASE_URL = "http://192.168.1.5:8001/api/"
//
//    val instance: RecentJobService by lazy {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        retrofit.create(RecentJobService::class.java)
//    }

    private var recentJobService: RecentJobService? =null
    fun getApi(context: HomeFragment): RecentJobService {
        if(recentJobService == null){
            val apiUrl = context.getString(R.string.api_url)
            recentJobService = Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RecentJobService::class.java)
        }
        return  recentJobService!!
    }

}