package org.deblock.exercise.application.web

import jakarta.validation.Valid
import jakarta.validation.ValidationException
import org.deblock.exercise.application.dto.FlightResponseDto
import org.deblock.exercise.application.dto.FlightResponseDto.Companion.fromDomain
import org.deblock.exercise.application.dto.FlightSearchQueryDto
import org.deblock.exercise.application.service.FlightService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller responsible for handling flight search requests.
 */
@RestController
@RequestMapping("/flights")
class FlightsApiController(private val flightService: FlightService) {

    /**
     * Finds flights based on the provided [query] and returns a list of [FlightResponseDto].
     *
     * @param query The flight search query received from the client.
     * @return A list of [FlightResponseDto] sorted by fare.
     * @throws ValidationException if the query is invalid (e.g., origin equals destination).
     */
    @PostMapping("/find")
    fun findFlights(@Valid @RequestBody query: FlightSearchQueryDto): List<FlightResponseDto> {
        query.validateQuery()
        return flightService.findFlights(query.toDomain()).map { it.fromDomain() }
    }

    private fun FlightSearchQueryDto.validateQuery() {
        if (origin == destination) throw ValidationException("origin and destination must not be equal")
    }
}