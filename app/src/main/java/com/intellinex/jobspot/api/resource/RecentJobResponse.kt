package com.intellinex.jobspot.api.resource

data class RecentJobResponse(
    val code: Int,
    val status: String,
    val msg: String,
    val data: CareerData
)
