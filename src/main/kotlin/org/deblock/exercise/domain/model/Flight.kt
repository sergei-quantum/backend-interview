package org.deblock.exercise.domain.model

import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal
import java.time.LocalDateTime

data class Flight(
    val airline: String,
    val supplier: SupplierType, // replace with Type
    val fare: BigDecimal, // don't forget to apply rounding
    val departureAirportCode: String,
    val destinationAirportCode:String,
    val departureDate: LocalDateTime,
    val arrivalDate: LocalDateTime
)

enum class SupplierType(private val displayName: String) {
    CRAZY_AIR("CrazyAir"),
    TOUGH_JET("ToughJet");

    @JsonValue
    fun toJson(): String = displayName
}