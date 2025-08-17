package org.deblock.exercise.domain.model

import java.time.LocalDate

/**
 * Represents a search query for flights.
 */
data class FlightSearchQuery(
    /**
     * The IATA code of the departure airport.
     */
    val origin: String,
    /**
     * The IATA code of the arrival airport.
     */
    val destination: String,
    /**
     * The departure date of the flight.
     */
    val departureDate: LocalDate,
    /**
     * The return date of the flight.
     */
    val returnDate: LocalDate,
    /**
     * Number of passengers for the flight.
     */
    val numberOfPassengers: Int
)