package com.intellinex.jobspot.api.resource

data class FeedResponse(
    val code: Int,
    val `data`: FeedData,
    val msg: String,
    val status: String
)

data class FeedData(
    val current_page: Int,
    val `data`: List<FeedDataX>,
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

data class FeedDataX(
    val title: String?,
    val description: String,
    val company_logo: String,
    val company_name: String,
    val comment_count: Any?,
    val assets: List<String>?,
    val created_at: String,
    val id: Int,
    val reaction_count: Any?,
)