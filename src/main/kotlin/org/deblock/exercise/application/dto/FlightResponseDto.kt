package org.deblock.exercise.application.dto

import org.deblock.exercise.domain.model.Flight
import org.deblock.exercise.domain.model.SupplierType
import java.math.BigDecimal
import java.time.LocalDateTime

data class FlightResponseDto(
    val airline: String,
    val supplier: SupplierType,
    val fare: BigDecimal,
    val departureAirportCode: String,
    val destinationAirportCode: String,
    val departureDate: LocalDateTime,
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