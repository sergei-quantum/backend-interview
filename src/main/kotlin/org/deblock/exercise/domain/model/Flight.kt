package org.deblock.exercise.domain.model

import com.fasterxml.jackson.annotation.JsonValue
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Represents a flight with its details and pricing information.
 */
data class Flight(
    /**
     * The name of the airline operating the flight.
     */
    val airline: String,
    /**
     * The supplier providing this flight, of type [SupplierType].
     */
    val supplier: SupplierType,
    /**
     * The price of the flight.
     */
    val fare: BigDecimal,
    /**
     * The IATA code of the departure airport.
     */
    val departureAirportCode: String,
    /**
     * The IATA code of the arrival airport.
     */
    val destinationAirportCode:String,
    /**
     * The departure date and time of the flight.
     */
    val departureDate: LocalDateTime,
    /**
     * The arrival date and time of the flight.
     */
    val arrivalDate: LocalDateTime
)

enum class SupplierType(private val displayName: String) {
    CRAZY_AIR("CrazyAir"),
    TOUGH_JET("ToughJet");

    @JsonValue
    fun toJson(): String = displayName
}