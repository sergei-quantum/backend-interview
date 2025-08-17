package org.deblock.exercise.infrastructure.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigDecimal
import java.time.LocalDateTime

data class CrazyAirResponseDto(
    val airline: String,
    val price: BigDecimal,
    val cabinclass: CabinClass,
    val departureAirportCode: String,
    val destinationAirportCode: String,
    val departureDate: LocalDateTime, // ISO_LOCAL_DATE_TIME format
    val arrivalDate: LocalDateTime // ISO_LOCAL_DATE_TIME format
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