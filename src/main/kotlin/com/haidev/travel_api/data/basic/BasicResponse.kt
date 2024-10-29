package com.haidev.travel_api.data.basic

data class BasicResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)
