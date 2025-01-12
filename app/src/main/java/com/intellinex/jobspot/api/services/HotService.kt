package com.intellinex.jobspot.api.services

import com.intellinex.jobspot.api.resource.HotResponse
import retrofit2.Call
import retrofit2.http.POST

interface HotService {
    @POST("hot/read")
    fun getHots(): Call<HotResponse>
}