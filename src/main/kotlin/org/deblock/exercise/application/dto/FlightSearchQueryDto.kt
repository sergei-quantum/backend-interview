package org.deblock.exercise.application.dto

import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import org.deblock.exercise.domain.model.FlightSearchQuery
import java.time.LocalDate

data class FlightSearchQueryDto(
    @field:Size(min = 3, max = 3, message = "origin must be 3 characters IATA code")
    val origin: String,

    @field:Size(min = 3, max = 3, message = "destination must be 3 characters IATA code")
    val destination: String,

    @field:FutureOrPresent(message = "departureDate must be a date in the present or in the future")
    val departureDate: LocalDate,

    @field:FutureOrPresent(message = "returnDate must be a date in the present or in the future")
    val returnDate: LocalDate,

    @field:Min(1, message = "at least 1 passenger required")
    @field:Max(4, message = "maximum 4 passengers allowed")
    val numberOfPassengers: Int
) {
    fun toDomain(): FlightSearchQuery {
        return FlightSearchQuery(
            origin = origin,
            destination = destination,
            departureDate = departureDate,
            returnDate = returnDate,
            numberOfPassengers = numberOfPassengers,
        )
    }
}