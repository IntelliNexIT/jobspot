package com.intellinex.jobspot.api.resource

data class ApiResponse(
    val code: Int,
    val status: String,
    val msg: String,
    val data: CareerData
)

data class CareerData(
    val current_page: Int,
    val data: List<Job>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<Link>,
    val next_page_url: String?,
    val path: String,
    val per_page: Int,
    val prev_page_url: String?,
    val to: Int,
    val total: Int
)

data class Job(
    val id: Int,
    val title: String,
    val description: String,
    val salary: String?,
    val unit: String,
    val skills: String,
    val company_name: String,
    val company_profile: String,
    val industry_name: String,
    val location_name: String,
    val workspace: String,
    val employment_type: String,
    val status: String,
    val created_at: String?,
    val updated_at: String?
)

data class Link(
    val url: String?,
    val label: String,
    val active: Boolean
)