package org.deblock.exercise.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class Flight(
    val airline: String,
    val supplier: SupplierType, // replace with Type
    val fare: BigDecimal, // don't forget to apply rounding
    val departureAirportCode: String,
    val destinationAirportCode:String,
    val departureDate: LocalDate,
    val arrivalDate: LocalDate
)

enum class SupplierType(name: String) {
    CRAZY_AIR("CrazyAir"),
    TOUGH_JET("ToughJet"),
}