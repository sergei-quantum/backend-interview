package org.deblock.exercise.infrastructure.dto

import java.math.BigDecimal
import java.time.Instant

data class ToughJetResponseDto(
    /**
     * Name of the airline carrier.
     */
    val carrier: String,
    /**
     * Base price of the flight before taxes and discounts.
     */
    val basePrice: BigDecimal,
    /**
     * Tax which needs to be charged along with the price.
     */
    val tax: BigDecimal,
    /**
     * Discount which needs to be applied on the price(in percentage).
     */
    val discount: BigDecimal,
    /**
     * IATA code of the departure airport.
     */
    val departureAirportName: String,
    /**
     * IATA code of the arrival airport.
     */
    val arrivalAirportName: String,
    /**
     * Departure date and time in ISO_INSTANT format.
     */
    val outboundDateTime: Instant,
    /**
     * Return/arrival date and time in ISO_INSTANT format.
     */
    val inboundDateTime: Instant
)