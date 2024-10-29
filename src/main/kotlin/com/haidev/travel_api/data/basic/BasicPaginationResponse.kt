package com.haidev.travel_api.data.basic

data class BasicPaginationResponse<T>(
    val success: Boolean,
    val message: String,
    val page: Int? = null,
    val perPage: Int? = null,
    val totalPage: Int? = null,
    val todalData: Int? = null,
    val data: T? = null,
)
