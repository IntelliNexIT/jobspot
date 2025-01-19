package com.intellinex.jobspot.api.instances

import android.content.Context
import com.intellinex.jobspot.R
import com.intellinex.jobspot.api.services.ContentService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ContentServiceProvider {

    private var service: ContentService? = null
    fun getApi(context: Context): ContentService {
        if(service == null){
            val apiUrl = context.getString(R.string.api_url)
            service = Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ContentService::class.java)
        }
        return service!!
    }
}