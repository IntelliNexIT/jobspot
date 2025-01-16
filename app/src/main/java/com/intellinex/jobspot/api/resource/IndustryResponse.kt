package com.intellinex.jobspot.api.resource

data class IndustryResponse(
    val code: Int,
    val `data`: DataIndustry,
    val msg: String,
    val status: String
)


data class DataIndustry(
    val current_page: Int,
    val `data`: List<DataXIndustry>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val links: List<Link>,
    val next_page_url: Any,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int,
    val total: Int
)

data class DataXIndustry(
    val id: Int,
    val name: String,
    val sub_industries: String?,
    val created_at: String,
    val updated_at: String,
    var is_checked: Boolean
)