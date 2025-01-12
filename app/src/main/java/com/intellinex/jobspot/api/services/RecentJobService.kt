package com.intellinex.jobspot.api.services

import com.intellinex.jobspot.api.resource.RecentJobResponse
import retrofit2.Call
import retrofit2.http.POST

interface RecentJobService {

    @POST("career/read")
    fun getRecentJob(): Call<RecentJobResponse>

}