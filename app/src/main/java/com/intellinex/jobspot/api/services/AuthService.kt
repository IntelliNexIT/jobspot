package com.intellinex.jobspot.api.services

import com.intellinex.jobspot.api.resource.SignInResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class SignInRequest(
    val email: String,
    val password: String
)

interface AuthService {
    @POST("auth/signin")
    fun signin(@Body request: SignInRequest): Call<SignInResponse>
}