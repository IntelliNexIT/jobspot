package com.intellinex.jobspot.api.resource

data class JobTypeResponse(
    val code: Int,
    val `data`: List<DataJobType>,
    val msg: String,
    val status: String
)
data class DataJobType(
    val id: Int,
    val title: String,
    val description: String,
    val created_at: String,
    val updated_at: String,
    var is_check: Boolean
)