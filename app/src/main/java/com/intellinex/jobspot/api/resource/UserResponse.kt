package com.intellinex.jobspot.api.resource

data class UserResponse(
    val code: Int,
    val `data`: User,
    val msg: String
)

data class User(
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
    val updated_at: String,
    val username: String,
    val verification: String,
    val location: String?
)