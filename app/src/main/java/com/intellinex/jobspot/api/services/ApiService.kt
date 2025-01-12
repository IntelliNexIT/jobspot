package com.intellinex.jobspot.api.services

import com.intellinex.jobspot.api.resource.ApiResponse
import retrofit2.Call
import retrofit2.http.POST

interface ApiService {
    @POST("career/read")
    fun getCareers() :Call<ApiResponse>
}