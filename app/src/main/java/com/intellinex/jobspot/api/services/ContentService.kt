package com.intellinex.jobspot.api.services

import com.intellinex.jobspot.api.resource.content.CareerDetailResponse
import retrofit2.http.Body
import retrofit2.http.POST

data class CareerDetailRequest (
    val id: Int
)

interface ContentService {

    @POST("career/find")
    suspend fun getCareerDetail(@Body body: CareerDetailRequest): CareerDetailResponse

}