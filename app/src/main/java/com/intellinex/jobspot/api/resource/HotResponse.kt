package com.intellinex.jobspot.api.resource

data class HotResponse(
    val code: Int,
    val status: String,
    val msg: String,
    val data: List<Data>
)

data class Data(
    val id: Int,
    val name: String?,
    val description: String?,
    val image: String,
    val created_at: String?,
    val updated_at: String?
)