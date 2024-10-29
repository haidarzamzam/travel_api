package com.haidev.travel_api.data.user

data class DataUser(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String? = null,
    val avatar: String? = null,
    val token: String
)