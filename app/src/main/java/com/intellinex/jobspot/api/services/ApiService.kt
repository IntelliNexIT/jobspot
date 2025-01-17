package com.intellinex.jobspot.api.services

import com.intellinex.jobspot.api.resource.ApiResponse
import com.intellinex.jobspot.api.resource.FeedResponse
import com.intellinex.jobspot.api.resource.IndustryResponse
import com.intellinex.jobspot.api.resource.JobTypeResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("career/index")
    fun getCareers() :Call<ApiResponse>

    @POST("industry/index")
    fun getIndustries(): Call<IndustryResponse>

    @POST("employment_type/index")
    fun getJobTypes(): Call<JobTypeResponse>

    @POST("feeds/index")
    fun getFeeds(@Query("page") page: Int?): Call<FeedResponse>
}