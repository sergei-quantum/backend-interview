package org.deblock.exercise.infrastructure.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

/**
 * DTO representing the request to the ToughJet supplier API.
 */
data class ToughJetRequestDto(
    /**
     * IATA code of the departure airport
     */
    val from: String,
    /**
     * IATA code of the destination airport
     */
    val to: String,
    /**
     * Departure date and time in ISO_LOCAL_DATE_TIME format
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val outboundDate: LocalDateTime,
    /**
     * Arrival date and time in ISO_LOCAL_DATE_TIME format
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val arrivalDate: LocalDateTime,
    /**
     * Number of adult passengers for the flight
     */
    val numberOfAdults: Int
)
