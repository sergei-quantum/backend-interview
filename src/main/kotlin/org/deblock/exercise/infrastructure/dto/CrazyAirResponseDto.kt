package org.deblock.exercise.infrastructure.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * DTO representing the response from the CrazyAir supplier API.
 */
data class CrazyAirResponseDto(
    /**
     * Name of the airline providing the flight
     */
    val airline: String,
    /**
     * Total price of the flight
     */
    val price: BigDecimal,
    /**
     * Cabin class of the flight
     */
    val cabinclass: CabinClass,
    /**
     * IATA code of the departure airport
     */
    val departureAirportCode: String,
    /**
     * IATA code of the destination airport
     */
    val destinationAirportCode: String,
    /**
     * Departure date and time in ISO_LOCAL_DATE_TIME format
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val departureDate: LocalDateTime,
    /**
     * Arrival date and time in ISO_LOCAL_DATE_TIME format
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val arrivalDate: LocalDateTime
)

enum class CabinClass(val code: String) {
    ECONOMY("E"),
    BUSINESS("B");

    companion object {
        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun fromCode(code: String): CabinClass {
            return values().find { it.code == code }
                ?: throw IllegalArgumentException("Unknown code: $code")
        }
    }
}