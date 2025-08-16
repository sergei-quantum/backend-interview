package org.deblock.exercise.domain.model

import java.time.LocalDate

data class FlightQuery(
    val origin: String,
    val destination: String,
    val departureDate: LocalDate,
    val returnDate: LocalDate,
    val numberOfPassengers: Int // Maximum 4 passengers
)
// TODO add validations