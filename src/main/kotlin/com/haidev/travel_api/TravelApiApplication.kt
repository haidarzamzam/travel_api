package com.haidev.travel_api

import com.haidev.travel_api.data.basic.BasicPaginationResponse
import com.haidev.travel_api.data.basic.BasicResponse
import com.haidev.travel_api.data.travel.TravelRequest
import com.haidev.travel_api.data.user.DataUser
import com.haidev.travel_api.data.user.LoginRequest
import com.haidev.travel_api.source.TravelRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@SpringBootApplication(scanBasePackages = ["com.haidev.travel_api", "com.haidev.travel_api.source"])
class TravelApiApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<TravelApiApplication>(*args)
        }
    }

    @Autowired
    private lateinit var travelRepository: TravelRepository

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<BasicResponse<DataUser>> {
        val email = request.email
        val password = request.password

        if (email == "phincon@academy.com" && password == "password") {
            val token =
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"

            val data = DataUser(
                firstName = "John",
                lastName = "Doe",
                email = email,
                phone = "08123456789",
                avatar = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjr1sJ9GLmlT0dxCedk2yfiabM3s07EOfaxw&s",
                token = token
            )

            return ResponseEntity.ok(BasicResponse(success = true, message = "Login success", data = data))
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(BasicResponse(success = false, message = "Invalid email or password"))
        }
    }

    @GetMapping("/travel")
    fun travel(
        @RequestHeader("Authorization") authorizationHeader: String?,
        @RequestParam("page") page: Int = 1,
        @RequestParam("per_page") perPage: Int = 5,
        @RequestParam("type") type: String?,
        @RequestParam("name") name: String?
    ): ResponseEntity<Any> {
        if (!isAuthorized(authorizationHeader)) {
            return unauthorizedResponse()
        }


        val data =
            when {
                type != null && name != null -> travelRepository.findDestinationByTypeAndName(
                    type.lowercase(),
                    name.lowercase(),
                    PageRequest.of(page - 1, perPage)
                )

                type != null -> travelRepository.findDestinationByType(
                    type.lowercase(),
                    PageRequest.of(page - 1, perPage)
                )

                name != null -> travelRepository.findDestinationByName(
                    name.lowercase(),
                    PageRequest.of(page - 1, perPage)
                )

                else -> travelRepository.findDestination(PageRequest.of(page - 1, perPage))
            }

        val response: ResponseEntity<Any> = ResponseEntity.ok(
            BasicPaginationResponse(
                success = true,
                message = "Success",
                page = page,
                perPage = perPage,
                totalPage = data.totalPages,
                todalData = data.totalElements.toInt(),
                data = data.content
            )
        )

        return response
    }

    @GetMapping("/travel/{id}")
    fun travelById(
        @RequestHeader("Authorization") authorizationHeader: String?,
        @PathVariable("id") id: Long
    ): ResponseEntity<Any> {
        if (!isAuthorized(authorizationHeader)) {
            return unauthorizedResponse()
        }

        val data = travelRepository.findDestinationById(id)

        val response: ResponseEntity<Any> = ResponseEntity.ok(
            BasicResponse(
                success = true,
                message = "Success",
                data = data
            )
        )

        return response
    }

    @PostMapping("/travel")
    fun addDestination(
        @RequestHeader("Authorization") authorizationHeader: String?,
        @RequestBody travelRequest: TravelRequest
    ): ResponseEntity<Any> {
        if (!isAuthorized(authorizationHeader)) {
            return unauthorizedResponse()
        }

        travelRepository.addDestination(travelRequest)

        val response: ResponseEntity<Any> = ResponseEntity.ok(
            BasicResponse(
                success = true,
                message = "Success",
                data = travelRequest
            )
        )

        return response
    }

    @PutMapping("/travel/{id}")
    fun updateDestination(
        @RequestHeader("Authorization") authorizationHeader: String?,
        @PathVariable("id") id: Long,
        @RequestBody travelRequest: TravelRequest
    ): ResponseEntity<Any> {
        if (!isAuthorized(authorizationHeader)) {
            return unauthorizedResponse()
        }

        travelRepository.updateDestination(id, travelRequest)

        val response: ResponseEntity<Any> = ResponseEntity.ok(
            BasicResponse(
                success = true,
                message = "Success",
                data = travelRequest
            )
        )

        return response
    }


    @DeleteMapping("/travel/{id}")
    fun deleteDestination(
        @RequestHeader("Authorization") authorizationHeader: String?,
        @PathVariable("id") id: Long
    ): ResponseEntity<Any> {
        if (!isAuthorized(authorizationHeader)) {
            return unauthorizedResponse()
        }

        travelRepository.deleteDestination(id)

        val response: ResponseEntity<Any> = ResponseEntity.ok(
            BasicResponse(
                success = true,
                message = "Success",
                data = "Data with id $id has been deleted"
            )
        )

        return response
    }

    //get travel type
    @GetMapping("/travel/type")
    fun travelType(
        @RequestHeader("Authorization") authorizationHeader: String?
    ): ResponseEntity<Any> {
        if (!isAuthorized(authorizationHeader)) {
            return unauthorizedResponse()
        }

        val data = travelRepository.findType()

        val response: ResponseEntity<Any> = ResponseEntity.ok(
            BasicResponse(
                success = true,
                message = "Success",
                data = data
            )
        )

        return response
    }

    private fun isAuthorized(authorizationHeader: String?): Boolean {
        return authorizationHeader == "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
    }

    private fun unauthorizedResponse(): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(
                BasicResponse<String>(
                    success = false,
                    message = "Unauthorized"
                )
            )
    }


}