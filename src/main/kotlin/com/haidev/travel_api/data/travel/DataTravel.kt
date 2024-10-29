package com.haidev.travel_api.data.travel

import jakarta.persistence.*

@Entity
@Table(name = "data_travel", schema = "travel_db")
data class DataTravel(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int = 0,
    @Column(name = "name") val name: String = "",
    @Column(name = "location") val location: String = "",
    @Column(name = "description") val description: String = "",
    @Column(name = "image") val image: String = "",
    @Column(name = "type") val type: String = "",
    @Column(name = "activity") val activity: String = "",
    @Column(name = "popularity") val popularity: String = "",
    @Column(name = "duration") val duration: String = "",
)