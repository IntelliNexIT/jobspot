package com.intellinex.jobspot.api.requests

data class CompanyRequest(
    val company_name: String,
    val user_id: Int,
    val industry_id: Int,
    val location_id: Int,
    val profile: String?,
    val bio: String?,
    val employee_count: Int?,
    val since: Any?,
    val website: String?,
    val gallery_images: String?,
    val status: String,
    val employment_size_id: Int?
)


data class CompanySelectByUser(
    val user_id: Int
)