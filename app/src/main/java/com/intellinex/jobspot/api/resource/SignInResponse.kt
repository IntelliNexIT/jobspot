package com.intellinex.jobspot.api.resource

data class SignInResponse(
    val code: Int,
    val `data`: SignInData,
    val msg: String,
    val status: String
)

data class SignInData(
    val access_token: String,
    val avatar: String,
    val bio: String,
    val birthday: String,
    val created_at: String,
    val email: String,
    val email_verified_at: String,
    val gender: String,
    val id: Int,
    val location_id: Int,
    val mobile: String,
    val nickname: String,
    val push_token: Any,
    val status: String,
    val token_type: String,
    val updated_at: String,
    val username: String,
    val verification: String
)