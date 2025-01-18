package com.intellinex.jobspot.api.services

import com.intellinex.jobspot.api.resource.SignInResponse
import com.intellinex.jobspot.api.resource.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class SignInRequest(
    val email: String,
    val password: String
)

data class LogoutResponse(
    val code: Int,
    val status: String,
    val msg: String,
    val data: Any
)

interface AuthService {
    @POST("auth/signin")
    fun signin(@Body request: SignInRequest): Call<SignInResponse>

    @POST("me")
    fun user(): Call<UserResponse>

    @POST("logout")
    suspend fun logout(): LogoutResponse
}