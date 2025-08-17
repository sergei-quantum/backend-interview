package org.deblock.exercise.infrastructure.dto

import java.time.LocalDate

/**
 * DTO representing a request to the CrazyAir supplier.
 */
data class CrazyAirRequestDto(
    /**
     * IATA code of the departure airport.
     */
    val origin: String,
    /**
     * IATA code of the destination airport.
     */
    val destination: String,
    /**
     * Departure date of the flight.
     */
    val departureDate: LocalDate,
    /**
     * Return date of the flight.
     */
    val returnDate: LocalDate,
    /**
     * Number of passengers for the flight.
     */
    val passengerCount: Int
)