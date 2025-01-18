package com.intellinex.jobspot.api.services

import com.intellinex.jobspot.api.requests.CompanySelectByUser
import com.intellinex.jobspot.api.resource.CompanyResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DataService {

    // COMPANY DATA
    @POST("company/selectByUser")
    fun getCompanyByUser(@Body request: CompanySelectByUser): Call<CompanyResponse>

}