package org.deblock.exercise.infrastructure.dto

import java.time.LocalDate

/// change to upper case queries
data class CrazyAirRequest(
    val origin: String,
    val destination: String,
    val departureDate: LocalDate, // use Iso format
    val returnDate: LocalDate, // use Iso format
    val passengerCount: Int
)