package com.intellinex.jobspot.api.instances

import com.intellinex.jobspot.api.services.RecentJobService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RecentJobInstance {

    private const val BASE_URL = "http://192.168.1.242:8001/api/"

    val instance: RecentJobService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RecentJobService::class.java)
    }

}