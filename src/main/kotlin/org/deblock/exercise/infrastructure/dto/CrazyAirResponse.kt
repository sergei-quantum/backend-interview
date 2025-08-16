package org.deblock.exercise.infrastructure.dto

import java.math.BigDecimal
import java.time.LocalDate

data class CrazyAirResponse(
    val airline: String,
    val price: BigDecimal,
    val cabinclass: String,
    val departureAirportCode: String,
    val destinationAirportCode: String,
    val departureDate: LocalDate, // ISO_LOCAL_DATE_TIME format
    val arrivalDate: LocalDate // ISO_LOCAL_DATE_TIME format
)