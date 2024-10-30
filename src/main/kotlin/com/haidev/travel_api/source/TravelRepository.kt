package com.haidev.travel_api.source

import com.haidev.travel_api.data.travel.DataTravel
import com.haidev.travel_api.data.travel.TravelRequest
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TravelRepository : JpaRepository<DataTravel, Long> {
    @Query(value = "SELECT * FROM data_travel", nativeQuery = true)
    fun findDestination(pageable: Pageable): Page<List<DataTravel>>


    @Query(value = "SELECT * FROM data_travel WHERE LOWER(type) = :type", nativeQuery = true)
    fun findDestinationByType(type: String, pageable: Pageable): Page<List<DataTravel>>

    @Query(value = "SELECT * FROM data_travel WHERE LOWER(name) LIKE %:name%", nativeQuery = true)
    fun findDestinationByName(name: String, pageable: Pageable): Page<List<DataTravel>>

    @Query(
        value = "SELECT * FROM data_travel WHERE LOWER(type) = :type AND LOWER(name) LIKE %:name%",
        nativeQuery = true
    )
    fun findDestinationByTypeAndName(type: String, name: String, pageable: Pageable): Page<List<DataTravel>>

    @Query(value = "SELECT * FROM data_travel WHERE id = :id", nativeQuery = true)
    fun findDestinationById(id: Long): DataTravel

    @Transactional
    @Modifying
    @Query(
        value = "INSERT INTO data_travel (name, location, description, image, type, activity, popularity, duration) VALUES (:#{#dataTravel.name}, :#{#dataTravel.location}, :#{#dataTravel.description}, :#{#dataTravel.image}, :#{#dataTravel.type}, :#{#dataTravel.activity}, :#{#dataTravel.popularity}, :#{#dataTravel.duration})",
        nativeQuery = true
    )
    fun addDestination(dataTravel: TravelRequest): Int

    @Transactional
    @Modifying
    @Query(
        value = "UPDATE data_travel SET name = :#{#dataTravel.name}, location = :#{#dataTravel.location}, description = :#{#dataTravel.description}, image = :#{#dataTravel.image}, type = :#{#dataTravel.type}, activity = :#{#dataTravel.activity}, popularity = :#{#dataTravel.popularity}, duration = :#{#dataTravel.duration} WHERE id = :id",
        nativeQuery = true
    )
    fun updateDestination(id: Long, dataTravel: TravelRequest): Int

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM data_travel WHERE id = :id", nativeQuery = true)
    fun deleteDestination(id: Long): Int

    //return list of type distinct
    @Query(value = "SELECT DISTINCT type FROM data_travel", nativeQuery = true)
    fun findType(): List<String>
}