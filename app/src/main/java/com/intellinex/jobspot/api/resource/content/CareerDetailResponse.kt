package com.intellinex.jobspot.api.resource.content

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CareerDetailResponse(
    val code: Int,
    val `data`: DataXCareer,
    val msg: String,
    val status: String
): Parcelable

@Parcelize
data class DataXCareer(
    val company_name: String,
    val company_profile: String,
    val created_at: String,
    val description: String,
    val employment_type: String,
    val id: Int,
    val industry_name: String,
    val location_name: String,
    val salary: Int,
    val skills: String,
    val experience_level: String,
    val followers: Int,
    val status: String,
    val title: String,
    val unit: String,
    val requirement: String?,
    val updated_at: String,
    val workspace: String
): Parcelable