package org.deblock.exercise.infrastructure.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class ToughJetResponseDto(
    val carrier: String,
    val basePrice: BigDecimal,
    val tax: BigDecimal,
    val discount: BigDecimal,
    val departureAirportName: String,
    val arrivalAirportName: String,
    val outboundDateTime: LocalDateTime, // ISO_INSTANT format
    val inboundDateTime: LocalDateTime // ISO_INSTANT format
)