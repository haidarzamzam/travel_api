package com.haidev.travel_api.data.travel

data class TravelRequest(
    val name: String,
    val location: String,
    val description: String,
    val image: String,
    val type: String,
    val activity: String,
    val popularity: String,
    val duration: String
)