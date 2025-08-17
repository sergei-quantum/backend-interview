package org.deblock.exercise.infrastructure.dto

import java.time.LocalDate

data class CrazyAirRequestDto(
    val origin: String,
    val destination: String,
    val departureDate: LocalDate,
    val returnDate: LocalDate,
    val passengerCount: Int
)