package com.intellinex.jobspot.api.resource

data class SelectAccountResponse(
    val code: Int,
    val `data`: List<DataCompany>,
    val msg: String,
    val status: String
)

data class DataCompany(
    val bio: String,
    val company_name: String,
    val created_at: String,
    val employee_count: String,
    val employment_size_id: Int,
    val follower: Int,
    val gallery_images: Any,
    val id: Int,
    val industry: String,
    val industry_id: Int,
    val location_id: Int,
    val profile: String,
    val since: String,
    val status: String,
    val updated_at: String,
    val user_id: Int,
    val website: Any
)