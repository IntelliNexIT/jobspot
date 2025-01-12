package com.intellinex.jobspot.api.instances

import com.intellinex.jobspot.R
import com.intellinex.jobspot.api.services.HotService
import com.intellinex.jobspot.ui.fragment.HomeFragment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HotInstance {

    private var hotService: HotService?=null

    fun getApi(context: HomeFragment): HotService{
        if(hotService == null){
            val apiUrl = context.getString(R.string.api_url)

            hotService = Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HotService::class.java)
        }
        return hotService!!
    }

//    val api: HotService by lazy {
//        Retrofit.Builder()
//            .baseUrl("http://172.10.0.247:8001/api/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(HotService::class.java)
//    }

}