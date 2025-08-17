package org.deblock.exercise.application.dto

import com.fasterxml.jackson.annotation.JsonFormat
import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.SupplierType
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * DTO representing a flight response returned by the API.
 */
data class FlightResponseDto(
    /**
     * Name of the airline.
     */
    val airline: String,
    /**
     * Supplier of the flight ([SupplierType]).
     */
    val supplier: SupplierType,
    /**
     * Flight price, rounded to two decimal places.
     */
    val fare: BigDecimal,
    /**
     * IATA code of the departure airport.
     */
    val departureAirportCode: String,
    /**
     * IATA code of the destination airport.
     */
    val destinationAirportCode: String,
    /**
     * Departure date and time of the flight.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val departureDate: LocalDateTime,
    /**
     * Arrival date and time of the flight.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val arrivalDate: LocalDateTime
) {
    companion object {
        fun Flight.fromDomain(): FlightResponseDto {
            return FlightResponseDto(
                airline = airline,
                supplier = supplier,
                fare = fare,
                departureAirportCode = departureAirportCode,
                destinationAirportCode = destinationAirportCode,
                departureDate = departureDate,
                arrivalDate = arrivalDate,
            )
        }
    }
}